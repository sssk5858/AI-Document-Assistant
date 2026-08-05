package com.sssk.backend.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;
import java.sql.SQLException;

@Converter(autoApply = false)
public class FloatArrayToVectorConverter implements AttributeConverter<float[], Object> {

    @Override
    public Object convertToDatabaseColumn(float[] attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("vector");
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < attribute.length; i++) {
                sb.append(attribute[i]);
                if (i < attribute.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            pgObject.setValue(sb.toString());
            return pgObject;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to convert float[] to PGobject vector", e);
        }
    }

    @Override
    public float[] convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        }
        
        String value;
        if (dbData instanceof PGobject pgobject) {
            value = pgobject.getValue();
        } else {
            value = dbData.toString();
        }

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        value = value.trim();
        if (value.startsWith("[") && value.endsWith("]")) {
            value = value.substring(1, value.length() - 1);
        }

        if (value.isEmpty()) {
            return new float[0];
        }

        String[] parts = value.split(",");
        float[] result = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Float.parseFloat(parts[i].trim());
        }
        return result;
    }
}

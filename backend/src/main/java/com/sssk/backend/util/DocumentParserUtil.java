package com.sssk.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import java.io.InputStream;

@Slf4j
public final class DocumentParserUtil {

    private static final Tika TIKA = new Tika();

    private DocumentParserUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Extracts text content from an InputStream using Apache Tika.
     * Tika automatically closes the InputStream after parsing is complete.
     *
     * @param inputStream the stream to read from
     * @return the extracted text as a String
     * @throws Exception if parsing fails
     */
    public static String extractText(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        return TIKA.parseToString(inputStream);
    }
}

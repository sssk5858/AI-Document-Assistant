package com.sssk.backend.mapper;

import com.sssk.backend.entity.Document;
import com.sssk.backend.dto.response.DocumentResponse;

public final class DocumentMapper {
    private DocumentMapper() {}

    public static DocumentResponse toResponse(Document document) {
        if (document == null) {
            return null;
        }
        return new DocumentResponse(
                document.getId(),
                document.getOriginalFileName(),
                document.getUploadStatus(),
                document.getFileSize(),
                document.getUploadedAt(),
                document.getExtractedText()
        );
    }
}

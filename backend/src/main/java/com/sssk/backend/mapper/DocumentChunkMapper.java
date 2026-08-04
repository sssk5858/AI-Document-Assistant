package com.sssk.backend.mapper;

import com.sssk.backend.entity.DocumentChunk;
import com.sssk.backend.dto.response.DocumentChunkResponse;

public final class DocumentChunkMapper {
    private DocumentChunkMapper() {}

    public static DocumentChunkResponse toResponse(DocumentChunk chunk) {
        if (chunk == null) {
            return null;
        }
        return new DocumentChunkResponse(
                chunk.getId(),
                chunk.getDocument() != null ? chunk.getDocument().getId() : null,
                chunk.getChunkIndex(),
                chunk.getChunkText(),
                chunk.getTokenCount(),
                chunk.getEmbeddingStatus() != null ? chunk.getEmbeddingStatus().name() : null,
                chunk.getCreatedAt()
        );
    }
}

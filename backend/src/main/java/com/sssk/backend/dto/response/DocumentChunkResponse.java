package com.sssk.backend.dto.response;

import java.time.LocalDateTime;

public record DocumentChunkResponse(
        Long id,
        Long documentId,
        Integer chunkIndex,
        String chunkText,
        Integer tokenCount,
        String embeddingStatus,
        LocalDateTime createdAt
) {
}

package com.sssk.backend.dto.response;

import java.time.LocalDateTime;

public record DocumentResponse(
        Long id,
        String originalFileName,
        String status,
        Long fileSize,
        LocalDateTime uploadedAt,
        String extractedText
) {
}

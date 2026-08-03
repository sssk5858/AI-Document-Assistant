package com.sssk.backend.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UploadDocumentRequest(MultipartFile file) {
}

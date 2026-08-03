package com.sssk.backend.service;

import com.sssk.backend.dto.response.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface DocumentService {
    DocumentResponse uploadDocument(MultipartFile file);
    List<DocumentResponse> getAllDocuments();
    DocumentResponse getDocument(Long id);
}

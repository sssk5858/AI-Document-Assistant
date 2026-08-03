package com.sssk.backend.controller;

import com.sssk.backend.dto.response.ApiResponse;
import com.sssk.backend.dto.response.DocumentResponse;
import com.sssk.backend.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DocumentResponse>> uploadDocument(@RequestParam("file") MultipartFile file) {
        DocumentResponse response = documentService.uploadDocument(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Document uploaded successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getAllDocuments() {
        List<DocumentResponse> response = documentService.getAllDocuments();
        return ResponseEntity.ok(ApiResponse.success("Retrieved all documents successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocument(@PathVariable("id") Long id) {
        DocumentResponse response = documentService.getDocument(id);
        return ResponseEntity.ok(ApiResponse.success("Retrieved document successfully", response));
    }
}

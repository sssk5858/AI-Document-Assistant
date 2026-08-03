package com.sssk.backend.service.impl;

import com.sssk.backend.entity.Document;
import com.sssk.backend.entity.DocumentStatus;
import com.sssk.backend.repository.DocumentRepository;
import com.sssk.backend.service.DocumentProcessingService;
import com.sssk.backend.service.TextExtractionService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentProcessingServiceImpl implements DocumentProcessingService {

    private final DocumentRepository documentRepository;
    private final MinioClient minioClient;
    private final TextExtractionService textExtractionService;

    @Override
    public void processDocument(Long documentId) {
        log.info("Starting background processing for document ID: {}", documentId);

        // 1. Fetch document and transition to PROCESSING
        Document document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            log.error("Document not found with ID: {}", documentId);
            return;
        }

        document.setStatus(DocumentStatus.PROCESSING);
        document = documentRepository.save(document);
        log.info("Document ID: {} status updated to PROCESSING", documentId);

        String extractedText = null;
        DocumentStatus finalStatus = DocumentStatus.FAILED;

        // 2. Download from MinIO & extract text
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(document.getBucketName())
                        .object(document.getObjectName())
                        .build())) {

            log.info("Downloading file from MinIO bucket '{}' object '{}'...", 
                    document.getBucketName(), document.getObjectName());
            extractedText = textExtractionService.extractText(stream);

            if (extractedText != null) {
                finalStatus = DocumentStatus.COMPLETED;
                log.info("Successfully extracted text from document ID: {}. Length: {} characters", 
                        documentId, extractedText.length());
            } else {
                log.warn("Text extraction returned null for document ID: {}", documentId);
            }

        } catch (Exception e) {
            log.error("Failed to process document ID: {} due to exception during file download/parsing", documentId, e);
        }

        // 3. Re-fetch/Update final status and extracted text in database
        Document updatedDocument = documentRepository.findById(documentId).orElse(document);
        updatedDocument.setExtractedText(extractedText);
        updatedDocument.setStatus(finalStatus);
        documentRepository.save(updatedDocument);
        log.info("Document ID: {} background processing completed with status: {}", documentId, finalStatus);
    }
}

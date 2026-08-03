package com.sssk.backend.service.impl;

import com.sssk.backend.config.MinioProperties;
import com.sssk.backend.constant.ApplicationConstants;
import com.sssk.backend.dto.response.DocumentResponse;
import com.sssk.backend.entity.Document;
import com.sssk.backend.exception.ResourceNotFoundException;
import com.sssk.backend.exception.StorageException;
import com.sssk.backend.mapper.DocumentMapper;
import com.sssk.backend.repository.DocumentRepository;
import com.sssk.backend.entity.DocumentStatus;
import com.sssk.backend.event.DocumentUploadedEvent;
import com.sssk.backend.service.DocumentService;
import com.sssk.backend.util.FileUtil;
import org.springframework.context.ApplicationEventPublisher;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public DocumentResponse uploadDocument(MultipartFile file) {
        // 1. Validate file
        FileUtil.validateFile(file);

        String originalFileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        long fileSize = file.getSize();

        // 2. Generate UUID object name
        String objectName = FileUtil.generateUUIDFileName(originalFileName);

        // 3. Upload file to MinIO
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), fileSize, -1)
                            .contentType(contentType)
                            .build()
            );
            log.info("Successfully uploaded file '{}' to MinIO as '{}' in bucket '{}'",
                    originalFileName, objectName, minioProperties.getBucketName());
        } catch (Exception e) {
            log.error("Failed to upload file '{}' to MinIO", originalFileName, e);
            throw new StorageException("Failed to upload file to object storage", e);
        }

        // 4. Create and save Document entity with UPLOADED status
        Document document = Document.builder()
                .originalFileName(originalFileName)
                .objectName(objectName)
                .bucketName(minioProperties.getBucketName())
                .contentType(contentType)
                .fileSize(fileSize)
                .status(DocumentStatus.UPLOADED)
                .uploadedAt(LocalDateTime.now())
                .build();

        Document savedDocument = documentRepository.save(document);
        log.info("Saved document metadata in database with ID: {}", savedDocument.getId());

        // 4.5. Publish event for asynchronous background processing
        try {
            eventPublisher.publishEvent(new DocumentUploadedEvent(savedDocument.getId()));
            log.info("Successfully published DocumentUploadedEvent for document ID: {}", savedDocument.getId());
        } catch (Exception e) {
            log.error("Failed to publish DocumentUploadedEvent for document ID: {}", savedDocument.getId(), e);
        }

        // 5. Convert and return response
        return DocumentMapper.toResponse(savedDocument);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(DocumentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentResponse getDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));
        return DocumentMapper.toResponse(document);
    }
}

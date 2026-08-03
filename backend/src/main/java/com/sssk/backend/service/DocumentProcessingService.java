package com.sssk.backend.service;

public interface DocumentProcessingService {

    /**
     * Process an uploaded document asynchronously in the background.
     * This loads the document, downloads it from MinIO, extracts text using Apache Tika,
     * and updates the document status and content in the database.
     *
     * @param documentId the ID of the document to process
     */
    void processDocument(Long documentId);
}

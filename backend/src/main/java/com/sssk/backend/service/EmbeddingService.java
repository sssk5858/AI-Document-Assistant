package com.sssk.backend.service;

import com.sssk.backend.entity.Document;

public interface EmbeddingService {
    /**
     * Generates and stores vector embeddings for all chunks of the given document.
     * Processes each chunk, calls the AI embedding provider, and updates the DB.
     *
     * @param document the document whose chunks should be embedded
     */
    void generateAndStoreEmbeddings(Document document);
}

package com.sssk.backend.service;

import com.sssk.backend.entity.Document;
import com.sssk.backend.entity.DocumentChunk;
import java.util.List;

public interface ChunkingService {
    /**
     * Splits document's extracted text into chunks and saves them in the database.
     * Deletes any existing chunks for this document first.
     *
     * @param document the document to chunk
     * @return the list of persisted DocumentChunk entities
     */
    List<DocumentChunk> chunkAndPersist(Document document);
}

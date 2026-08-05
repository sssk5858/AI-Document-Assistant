package com.sssk.backend.service.impl;

import com.sssk.backend.entity.ChunkEmbeddingStatus;
import com.sssk.backend.entity.Document;
import com.sssk.backend.entity.DocumentChunk;
import com.sssk.backend.repository.DocumentChunkRepository;
import com.sssk.backend.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingServiceImpl implements EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final DocumentChunkRepository chunkRepository;

    @Value("${spring.ai.google.genai.embedding.options.model:text-embedding-004}")
    private String modelName;

    @Override
    @Transactional
    public void generateAndStoreEmbeddings(Document document) {
        log.info("Starting embedding generation for document ID: {}", document.getId());

        List<DocumentChunk> chunks = chunkRepository.findByDocumentOrderByChunkIndexAsc(document);
        if (chunks.isEmpty()) {
            log.warn("No chunks found for document ID: {}", document.getId());
            return;
        }

        log.info("Found {} chunks to embed for document ID: {}", chunks.size(), document.getId());

        for (DocumentChunk chunk : chunks) {
            embedSingleChunk(chunk);
        }

        log.info("Completed embedding processing for document ID: {}", document.getId());
    }

    private void embedSingleChunk(DocumentChunk chunk) {
        log.debug("Embedding chunk index: {} for document ID: {}", chunk.getChunkIndex(), chunk.getDocument().getId());

        // 1. Transition chunk state to PROCESSING
        chunk.setEmbeddingStatus(ChunkEmbeddingStatus.PROCESSING);
        chunk = chunkRepository.saveAndFlush(chunk);

        try {
            // 2. Generate embedding vector using Spring AI client
            float[] vector = embeddingModel.embed(chunk.getChunkText());
            
            if (vector == null || vector.length == 0) {
                throw new IllegalStateException("Generated embedding is null or empty");
            }

            // 3. Store the vector and update metadata
            chunk.setEmbedding(vector);
            chunk.setEmbeddingModel(modelName);
            chunk.setEmbeddedAt(LocalDateTime.now());
            chunk.setEmbeddingStatus(ChunkEmbeddingStatus.COMPLETED);
            chunkRepository.save(chunk);
            
            log.debug("Successfully embedded chunk index: {} (dimensions: {})", chunk.getChunkIndex(), vector.length);
        } catch (Exception e) {
            log.error("Failed to generate embedding for chunk index: {} (document ID: {})", 
                    chunk.getChunkIndex(), chunk.getDocument().getId(), e);
            
            // 4. Mark chunk embedding as FAILED
            chunk.setEmbeddingStatus(ChunkEmbeddingStatus.FAILED);
            chunkRepository.save(chunk);
        }
    }
}

package com.sssk.backend.service.impl;

import com.sssk.backend.config.ChunkingProperties;
import com.sssk.backend.entity.ChunkEmbeddingStatus;
import com.sssk.backend.entity.Document;
import com.sssk.backend.entity.DocumentChunk;
import com.sssk.backend.repository.DocumentChunkRepository;
import com.sssk.backend.service.ChunkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.BreakIterator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChunkingServiceImpl implements ChunkingService {

    private final DocumentChunkRepository chunkRepository;
    private final ChunkingProperties chunkingProperties;

    @Override
    @Transactional
    public List<DocumentChunk> chunkAndPersist(Document document) {
        log.info("Starting chunking for document ID: {}", document.getId());

        // 1. Delete existing chunks for this document if any
        chunkRepository.deleteByDocument(document);

        String text = document.getExtractedText();
        if (text == null || text.isBlank()) {
            log.warn("Extracted text is empty for document ID: {}. No chunks created.", document.getId());
            return Collections.emptyList();
        }

        int chunkSize = chunkingProperties.getChunkSize();
        int chunkOverlap = chunkingProperties.getChunkOverlap();

        if (chunkSize <= chunkOverlap) {
            log.error("Invalid chunking configuration: chunkSize ({}) must be greater than chunkOverlap ({})", 
                    chunkSize, chunkOverlap);
            throw new IllegalArgumentException("Chunk size must be greater than chunk overlap");
        }

        // 2. Perform chunking using sentence break iterator
        List<String> chunkTexts = splitIntoChunks(text, chunkSize, chunkOverlap);
        log.info("Split document ID: {} into {} chunks", document.getId(), chunkTexts.size());

        // 3. Create and persist entities
        List<DocumentChunk> chunks = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int index = 0; index < chunkTexts.size(); index++) {
            String chunkText = chunkTexts.get(index);
            DocumentChunk chunk = DocumentChunk.builder()
                    .document(document)
                    .chunkIndex(index)
                    .chunkText(chunkText)
                    .tokenCount(chunkText.length()) // Using character count for now
                    .embeddingStatus(ChunkEmbeddingStatus.PENDING)
                    .createdAt(now)
                    .build();
            chunks.add(chunk);
        }

        List<DocumentChunk> savedChunks = chunkRepository.saveAll(chunks);
        log.info("Successfully persisted {} chunks for document ID: {}", savedChunks.size(), document.getId());
        return savedChunks;
    }

    private List<String> splitIntoChunks(String text, int chunkSize, int chunkOverlap) {
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(text);

        List<String> sentences = new ArrayList<>();
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String sentence = text.substring(start, end).trim();
            if (sentence.isEmpty()) {
                continue;
            }

            // Fallback for extremely long sentences/blocks without punctuation
            if (sentence.length() > chunkSize) {
                int idx = 0;
                while (idx < sentence.length()) {
                    int endIdx = Math.min(idx + chunkSize, sentence.length());
                    String sub = sentence.substring(idx, endIdx).trim();
                    if (!sub.isEmpty()) {
                        sentences.add(sub);
                    }
                    idx += chunkSize - chunkOverlap;
                    if (idx >= sentence.length() || chunkSize <= chunkOverlap) {
                        break;
                    }
                }
            } else {
                sentences.add(sentence);
            }
        }

        List<String> chunks = new ArrayList<>();
        if (sentences.isEmpty()) {
            return chunks;
        }

        int i = 0;
        while (i < sentences.size()) {
            int chunkStartIndex = i;
            StringBuilder currentChunk = new StringBuilder();
            int currentLength = 0;
            int lastAddedIndex = i;

            while (i < sentences.size()) {
                String sentence = sentences.get(i);
                if (currentLength > 0 && currentLength + sentence.length() + 1 > chunkSize) {
                    break;
                }
                if (currentLength > 0) {
                    currentChunk.append(" ");
                    currentLength += 1;
                }
                currentChunk.append(sentence);
                currentLength += sentence.length();
                lastAddedIndex = i;
                i++;
            }

            String chunkStr = currentChunk.toString().trim();
            if (!chunkStr.isEmpty()) {
                chunks.add(chunkStr);
            }

            // Overlap logic
            if (i < sentences.size()) {
                int nextStart = lastAddedIndex + 1;
                int overlapLength = 0;

                for (int k = lastAddedIndex; k > chunkStartIndex; k--) {
                    int sentenceLen = sentences.get(k).length();
                    if (overlapLength + sentenceLen > chunkOverlap) {
                        break;
                    }
                    overlapLength += sentenceLen + 1;
                    nextStart = k;
                }
                i = nextStart;
            }
        }
        return chunks;
    }
}

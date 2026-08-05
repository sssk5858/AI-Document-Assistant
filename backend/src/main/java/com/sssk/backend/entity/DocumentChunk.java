package com.sssk.backend.entity;

import com.sssk.backend.util.FloatArrayToVectorConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "document_chunks",
    indexes = {
        @Index(name = "idx_document_chunks_document_id", columnList = "document_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_document_chunks_doc_index", columnNames = {"document_id", "chunk_index"})
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(name = "chunk_index", nullable = false)
    private Integer chunkIndex;

    @Column(name = "chunk_text", columnDefinition = "TEXT", nullable = false)
    private String chunkText;

    @Column(name = "token_count", nullable = false)
    private Integer tokenCount;

    @Column(name = "embedding", columnDefinition = "vector")
    @Convert(converter = FloatArrayToVectorConverter.class)
    private float[] embedding;

    @Column(name = "embedding_model")
    private String embeddingModel;

    @Column(name = "embedded_at")
    private LocalDateTime embeddedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "embedding_status", nullable = false)
    private ChunkEmbeddingStatus embeddingStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.embeddingStatus == null) {
            this.embeddingStatus = ChunkEmbeddingStatus.PENDING;
        }
    }
}

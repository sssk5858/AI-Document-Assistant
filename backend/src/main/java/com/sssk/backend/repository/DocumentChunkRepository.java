package com.sssk.backend.repository;

import com.sssk.backend.entity.Document;
import com.sssk.backend.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {
    List<DocumentChunk> findByDocumentOrderByChunkIndexAsc(Document document);

    @Modifying
    @Query("DELETE FROM DocumentChunk c WHERE c.document = :document")
    void deleteByDocument(@Param("document") Document document);
}

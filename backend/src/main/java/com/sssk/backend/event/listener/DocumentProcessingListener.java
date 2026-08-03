package com.sssk.backend.event.listener;

import com.sssk.backend.event.DocumentUploadedEvent;
import com.sssk.backend.service.DocumentProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentProcessingListener {

    private final DocumentProcessingService documentProcessingService;

    /**
     * Listen for DocumentUploadedEvent and trigger document processing asynchronously.
     *
     * @param event the published document uploaded event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDocumentUploadedEvent(DocumentUploadedEvent event) {
        log.info("Received document uploaded event asynchronously for document ID: {} on thread {}", 
                event.documentId(), Thread.currentThread().getName());
        try {
            documentProcessingService.processDocument(event.documentId());
        } catch (Exception e) {
            log.error("Unhandled exception processing document ID: {}", event.documentId(), e);
        }
    }
}

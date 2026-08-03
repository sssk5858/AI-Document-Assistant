package com.sssk.backend.service.impl;

import com.sssk.backend.service.TextExtractionService;
import com.sssk.backend.util.DocumentParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Service
@Slf4j
public class TextExtractionServiceImpl implements TextExtractionService {

    @Override
    public String extractText(InputStream inputStream) {
        if (inputStream == null) {
            log.warn("Cannot extract text: InputStream is null");
            return null;
        }
        try {
            log.info("Initiating document text extraction using Apache Tika...");
            String text = DocumentParserUtil.extractText(inputStream);
            int length = text != null ? text.trim().length() : 0;
            log.info("Successfully extracted text. Characters: {}", length);
            return text;
        } catch (Exception e) {
            log.error("Failed to extract text using Apache Tika. Proceeding with null/empty content.", e);
            return null;
        }
    }
}

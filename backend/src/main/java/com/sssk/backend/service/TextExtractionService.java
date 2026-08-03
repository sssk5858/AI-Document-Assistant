package com.sssk.backend.service;

import java.io.InputStream;

public interface TextExtractionService {

    /**
     * Extracts text content from an InputStream.
     *
     * @param inputStream the stream to extract text from
     * @return the extracted text, or null if extraction fails
     */
    String extractText(InputStream inputStream);
}

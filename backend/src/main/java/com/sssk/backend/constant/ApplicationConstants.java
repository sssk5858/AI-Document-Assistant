package com.sssk.backend.constant;

import java.util.List;

public final class ApplicationConstants {
    private ApplicationConstants() {}

    public static final String UPLOAD_STATUS_PENDING = "PENDING";
    public static final String UPLOAD_STATUS_SUCCESS = "SUCCESS";
    public static final String UPLOAD_STATUS_FAILED = "FAILED";

    public static final long MAX_FILE_SIZE_BYTES = 100 * 1024 * 1024L; // 100MB

    public static final List<String> SUPPORTED_EXTENSIONS = List.of("pdf", "txt", "doc", "docx", "png", "jpg", "jpeg");
}

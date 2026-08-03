package com.sssk.backend.util;

import com.sssk.backend.constant.ApplicationConstants;
import com.sssk.backend.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public final class FileUtil {
    private FileUtil() {}

    public static String generateUUIDFileName(String originalFileName) {
        String ext = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);
    }

    public static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    public static void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Upload failed: File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new BusinessException("Upload failed: Original filename is missing");
        }

        String extension = getFileExtension(originalFilename);
        if (!ApplicationConstants.SUPPORTED_EXTENSIONS.contains(extension)) {
            throw new BusinessException("Upload failed: Unsupported file extension '." + extension + "'");
        }

        if (file.getSize() > ApplicationConstants.MAX_FILE_SIZE_BYTES) {
            throw new BusinessException("Upload failed: File size exceeds limit of " + 
                    (ApplicationConstants.MAX_FILE_SIZE_BYTES / (1024 * 1024)) + "MB");
        }
    }
}

package com.sssk.backend.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
                log.info("Successfully created MinIO bucket: {}", minioProperties.getBucketName());
            } else {
                log.info("MinIO bucket '{}' already exists.", minioProperties.getBucketName());
            }
        } catch (Exception e) {
            log.warn("Could not connect to MinIO or initialize bucket '{}' on startup: {}. Please ensure MinIO service is running.", 
                    minioProperties.getBucketName(), e.getMessage());
        }
        return minioClient;
    }
}


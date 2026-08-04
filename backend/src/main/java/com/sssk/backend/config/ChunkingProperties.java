package com.sssk.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.chunking")
public class ChunkingProperties {
    private int chunkSize = 1000;
    private int chunkOverlap = 200;
}

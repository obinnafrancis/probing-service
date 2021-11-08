package com.vlad.discovery.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@Builder
public class ServiceTracker {
    @NonNull
    private String serviceName;
    @NonNull
    private String status;
    @NonNull
    private LocalDateTime timestamp;
}

package com.vlad.discovery.service.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class ServiceModel {
    @NonNull
    private String serviceName;
    @NonNull
    private String probeUrl;
    @NonNull
    private transient String statusFIeldName;
    private transient String expectedValue;
    private LocalDateTime lastUpdatedOn;
    private boolean down;
}

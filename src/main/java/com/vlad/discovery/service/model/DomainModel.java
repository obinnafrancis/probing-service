package com.vlad.discovery.service.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class DomainModel {
    @NonNull
    private String domainName;
    @NonNull
    private transient String password;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
}

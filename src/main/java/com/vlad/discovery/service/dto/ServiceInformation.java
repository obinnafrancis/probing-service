package com.vlad.discovery.service.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document
public class ServiceInformation {
    @Id
    private transient String serviceId;
    private String domainId;
    private String serviceName;
    private String statusFieldName;
    private String statusExpectedValue;
    private String url;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedOn;
    private boolean down;
}

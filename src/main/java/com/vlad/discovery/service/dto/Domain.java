package com.vlad.discovery.service.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document
public class Domain {
    @Id
    private transient String appId;
    private String clientId;
    private String clientSecret;
    private String domainName;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedOn;
}

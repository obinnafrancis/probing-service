package com.vlad.discovery.service.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailResponse {
    private String response;
    private String message;
}

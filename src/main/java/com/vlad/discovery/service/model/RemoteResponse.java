package com.vlad.discovery.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemoteResponse {
    private int code;
    private String responseBody;
}

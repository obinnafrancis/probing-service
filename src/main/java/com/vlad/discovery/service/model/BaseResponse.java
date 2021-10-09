package com.vlad.discovery.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse {
    private String code;
    private String description;
    private Object data;
    private Object errors;
}

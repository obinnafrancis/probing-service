package com.vlad.discovery.service.service;

import com.vlad.discovery.service.model.BaseResponse;
import com.vlad.discovery.service.model.ServiceModel;
import org.springframework.http.ResponseEntity;

public interface ProbeService {
    public ResponseEntity<BaseResponse> createService(ServiceModel serviceModel);
    public void updateService(ServiceModel serviceModel);
    public void deleteService(String serviceName);
    public ResponseEntity<BaseResponse> getService(String serviceName);
}

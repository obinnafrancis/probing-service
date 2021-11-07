package com.vlad.discovery.service.service;

import com.vlad.discovery.service.dto.ServiceTracker;
import com.vlad.discovery.service.model.BaseResponse;
import com.vlad.discovery.service.model.DomainModel;
import org.springframework.http.ResponseEntity;

public interface TrackerService {
    public void logStatus(ServiceTracker serviceTracker);
    public ResponseEntity<BaseResponse> getAllLogs();
    public ResponseEntity<BaseResponse> getServiceLog(String serviceName);
}

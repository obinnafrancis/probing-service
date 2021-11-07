package com.vlad.discovery.service.service.impl;

import com.vlad.discovery.service.dao.DomainRepository;
import com.vlad.discovery.service.dao.ProbeServiceRepository;
import com.vlad.discovery.service.dao.TrackerRepository;
import com.vlad.discovery.service.dto.Domain;
import com.vlad.discovery.service.dto.ServiceTracker;
import com.vlad.discovery.service.model.BaseResponse;
import com.vlad.discovery.service.model.DomainModel;
import com.vlad.discovery.service.service.DomainService;
import com.vlad.discovery.service.service.TrackerService;
import com.vlad.discovery.service.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TrackerServiceImpl implements TrackerService {
    @Autowired
    TrackerRepository trackerRepository;

    @Override
    public void logStatus(ServiceTracker serviceTracker) {
        trackerRepository.logRecord(serviceTracker);
    }

    @Override
    public ResponseEntity<BaseResponse> getAllLogs() {
        return ResponseEntity.ok(BaseResponse.builder().data(trackerRepository.getAllServiceTrackers()).code("200").description("Successful").build());
    }

    @Override
    public ResponseEntity<BaseResponse> getServiceLog(String serviceName) {
        return ResponseEntity.ok(BaseResponse.builder().data(trackerRepository.getServiceLogs(serviceName)).code("200").description("Successful").build());
    }
}

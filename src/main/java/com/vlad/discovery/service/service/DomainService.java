package com.vlad.discovery.service.service;

import com.vlad.discovery.service.model.BaseResponse;
import com.vlad.discovery.service.model.DomainModel;
import com.vlad.discovery.service.model.ServiceModel;
import org.springframework.http.ResponseEntity;

public interface DomainService {
    public ResponseEntity<BaseResponse> createDomain(DomainModel domainModel);
    public void updateDomain(DomainModel domainModel);
    public void deleteDomain(String domainId);
    public ResponseEntity<BaseResponse> getDomain(String domainId);
    public ResponseEntity<BaseResponse> getDomainServices(String domainId);
}

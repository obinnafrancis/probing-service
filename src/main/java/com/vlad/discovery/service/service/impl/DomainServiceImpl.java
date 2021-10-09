package com.vlad.discovery.service.service.impl;

import com.vlad.discovery.service.dao.DomainRepository;
import com.vlad.discovery.service.dao.ProbeServiceRepository;
import com.vlad.discovery.service.dto.Domain;
import com.vlad.discovery.service.model.BaseResponse;
import com.vlad.discovery.service.model.DomainModel;
import com.vlad.discovery.service.service.DomainService;
import com.vlad.discovery.service.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class DomainServiceImpl implements DomainService {
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    ProbeServiceRepository probeServiceRepository;

    @Override
    public ResponseEntity<BaseResponse> createDomain(DomainModel domainModel) {
        Domain domain = Domain.builder()
                .clientId(Utils.generateUniqueClientId())
                .clientSecret(Utils.encodePassword(domainModel.getPassword()))
                .createdDate(LocalDateTime.now())
                .domainName(domainModel.getDomainName())
                .lastUpdatedOn(LocalDateTime.now())
                .build();
        return null;
    }

    @Override
    public void updateDomain(DomainModel domainModel) {

    }

    @Override
    public void deleteDomain(String domainId) {
        domainRepository.deleteDomain(domainId);
    }

    @Override
    public ResponseEntity<BaseResponse> getDomain(String domainId) {
        Optional<Domain> result = domainRepository.getByDomainId(domainId);
        if(!result.isPresent()){
            return new ResponseEntity<BaseResponse>(BaseResponse.builder().code("404").description("No Record Found").build(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(BaseResponse.builder().code("200").description("Successful").data(result.get()).build());
    }

    @Override
    public ResponseEntity<BaseResponse> getDomainServices(String domainId) {
        return ResponseEntity.ok(BaseResponse.builder().data(probeServiceRepository.getDomainServices(domainId)).code("200").description("Successful").build());
    }
}

package com.vlad.discovery.service.service.impl;

import com.vlad.discovery.service.dao.ProbeServiceRepository;
import com.vlad.discovery.service.dto.ServiceInformation;
import com.vlad.discovery.service.model.BaseResponse;
import com.vlad.discovery.service.model.ServiceModel;
import com.vlad.discovery.service.service.ProbeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProbeServiceImpl implements ProbeService {
    @Autowired
    ProbeServiceRepository probeServiceRepository;

    @Override
    public ResponseEntity<BaseResponse> createService(ServiceModel serviceModel) {
        ServiceInformation serviceInformation =
                ServiceInformation.builder()
                        .url(serviceModel.getProbeUrl())
                        .createdDate(LocalDateTime.now())
                        .domainId("domainId")
                        .lastUpdatedOn(LocalDateTime.now())
                        .serviceName(serviceModel.getServiceName())
                        .statusFieldName(serviceModel.getStatusFIeldName())
                        .statusExpectedValue(serviceModel.getExpectedValue())
                .build();
        try{
            var createResponse = probeServiceRepository.create(serviceInformation);
            return ResponseEntity.ok(BaseResponse.builder().code("200").description("Successful").data(createResponse).build());
        } catch (Exception e){
            return new ResponseEntity<BaseResponse>(BaseResponse.builder().code("200").description("Successful").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateService(ServiceModel serviceModel) {
        ServiceInformation serviceInformation =
                ServiceInformation.builder()
                        .url(serviceModel.getProbeUrl())
                        .domainId("domainId")
                        .lastUpdatedOn(LocalDateTime.now())
                        .serviceName(serviceModel.getServiceName())
                        .statusFieldName(serviceModel.getStatusFIeldName())
                        .statusExpectedValue(serviceModel.getExpectedValue())
                        .build();
        var result = probeServiceRepository.updateService(serviceInformation);
        if(result==null){
            throw new IllegalStateException("failed to update service");
        }
    }

    @Override
    public void deleteService(String serviceName) {
        probeServiceRepository.deleteService(serviceName,"domainId");
    }

    @Override
    public ResponseEntity<BaseResponse> getService(String serviceName) {
        Optional<ServiceInformation> result = probeServiceRepository.getOneService(serviceName,"domainId");
        if(!result.isPresent()){
            return new ResponseEntity<BaseResponse>(BaseResponse.builder().code("404").description("No Record Found").build(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(BaseResponse.builder().code("200").description("Successful").data(result.get()).build());
    }
}

package com.vlad.discovery.service.schedulers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.discovery.service.dao.ProbeServiceRepository;
import com.vlad.discovery.service.dto.ServiceInformation;
import com.vlad.discovery.service.model.RemoteResponse;
import com.vlad.discovery.service.util.RemoteCallUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SystemSchedulers {
    @Autowired
    RemoteCallUtil remoteCallUtil;
    @Autowired
    ProbeServiceRepository probeServiceRepository;

    @Scheduled(fixedRate = 10000,initialDelay = 5000)
    public void probingTask(){
        probeServiceRepository.getAllServices().stream().forEach(x->
        {
            boolean originalStatus = x.isDown();
            boolean currentStatus = false;
            String url = x.getUrl();
            RemoteResponse remoteResponse = remoteCallUtil.getCall(url,new HashMap());
            TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
            try {
                Map<String, Object> mapping = new ObjectMapper().readValue(remoteResponse.getResponseBody(),typeRef);
                String status = (String) mapping.get(x.getStatusFieldName());
                if(!x.getStatusExpectedValue().equalsIgnoreCase(status)){
                    currentStatus = true;
                }
                validateStateChange(originalStatus,currentStatus,x);
            } catch (JsonProcessingException e) {
                log.error("Failed to read response {}",e);
            }
        });
    }

    private void validateStateChange(boolean originalState, boolean currentState, ServiceInformation serviceInformation) {
        if(originalState^currentState){
            serviceInformation.setDown(currentState);
            ServiceInformation newState = ServiceInformation.builder()
                    .domainId(serviceInformation.getDomainId())
                    .down(serviceInformation.isDown())
                    .serviceName(serviceInformation.getServiceName())
                    .serviceId(serviceInformation.getServiceId())
                    .build();
            probeServiceRepository.updateService(newState);
            log.info("State change occured ... sending notification out for {}",serviceInformation.getStatusFieldName());
            sendMailOut(newState);
        }
    }

    private void sendMailOut(ServiceInformation newState) {
        log.info("Mail Sent");
    }
}

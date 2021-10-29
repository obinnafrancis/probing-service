package com.vlad.discovery.service.dao;

import com.mongodb.client.result.DeleteResult;
import com.vlad.discovery.service.dto.Domain;
import com.vlad.discovery.service.dto.ServiceInformation;
import com.vlad.discovery.service.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
public class ProbeServiceRepository {
    @Autowired
    MongoTemplate mongoTemplate;
    public static final String CLIENT_ID = "domainId";
    public static final String NAME = "serviceName";

    public ServiceInformation create(ServiceInformation serviceInformation){
        Criteria serviceNameCriteria = Criteria.where(NAME).is(serviceInformation.getServiceName());
        Criteria domainCriteria = Criteria.where(CLIENT_ID).is(serviceInformation.getDomainId());
        Query query = new Query(new Criteria().andOperator(Arrays.asList(serviceNameCriteria,domainCriteria)));
        try {
            mongoTemplate.save(serviceInformation);
            ServiceInformation createdService = mongoTemplate.findOne(query,ServiceInformation.class);
            createdService.setServiceId(null);
            return createdService;
        }catch (Exception e){
            log.error("An error occured while creating domain {}",e);
            return null;
        }
    }

    public Optional<ServiceInformation> getOneService(String serviceName, String domainId){
        Criteria serviceNameCriteria = Criteria.where(NAME).is(serviceName);
        Criteria domainCriteria = Criteria.where(CLIENT_ID).is(domainId);
        Query query = new Query(new Criteria().andOperator(Arrays.asList(serviceNameCriteria,domainCriteria)));
        return Optional.ofNullable(mongoTemplate.findOne(query,ServiceInformation.class));
    }

    public List<ServiceInformation>getAllServices(){
        return mongoTemplate.findAll(ServiceInformation.class);
    }

    public List<ServiceInformation> getDomainServices(String domainId){
        Criteria domainCriteria = Criteria.where(CLIENT_ID).is(domainId);
        Query query = new Query(new Criteria().andOperator(Arrays.asList(domainCriteria)));
        return mongoTemplate.find(query,ServiceInformation.class);
    }

    public void deleteService(String serviceName, String domainId){
        Criteria serviceNameCriteria = Criteria.where(NAME).is(serviceName);
        Criteria domainCriteria = Criteria.where(CLIENT_ID).is(domainId);
        Query query = new Query(new Criteria().andOperator(Arrays.asList(serviceNameCriteria,domainCriteria)));
        DeleteResult deleteResult = mongoTemplate.remove(query,ServiceInformation.class);
        if(!deleteResult.wasAcknowledged()){
            throw new IllegalArgumentException("record doesnt exist");
        }
    }

    public ServiceInformation updateService(ServiceInformation serviceInformation){
        Update update = new Update();
        update.set("lastUpdatedOn", LocalDateTime.now());
        update.set("down",serviceInformation.isDown());
        update.set("downCount",serviceInformation.getDownCount());
        if (!Utils.isNullorEmpty(serviceInformation.getStatusFieldName())) update.set("statusFieldName",serviceInformation.getStatusFieldName());
        if (!Utils.isNullorEmpty(serviceInformation.getUrl())) update.set("url",serviceInformation.getUrl());
        if (!Utils.isNullorEmpty(serviceInformation.getStatusExpectedValue())) update.set("statusExpectedValue",serviceInformation.getStatusExpectedValue());
        Criteria serviceNameCriteria = Criteria.where(NAME).is(serviceInformation.getServiceName());
        Criteria domainCriteria = Criteria.where(CLIENT_ID).is(serviceInformation.getDomainId());
        Query query = new Query(new Criteria().andOperator(Arrays.asList(serviceNameCriteria,domainCriteria)));
        try {
            mongoTemplate.findAndModify(query,update,ServiceInformation.class);
            ServiceInformation updatedValue = mongoTemplate.findOne(query,ServiceInformation.class);
            updatedValue.setServiceId(null);
            return updatedValue;
        }catch (Exception e){
            log.error("An error occured while updating domain {}",e);
            return null;
        }
    }
}

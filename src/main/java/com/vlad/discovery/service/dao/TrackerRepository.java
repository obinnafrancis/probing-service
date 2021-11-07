package com.vlad.discovery.service.dao;

import com.vlad.discovery.service.dto.ServiceTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class TrackerRepository {
    @Autowired
    MongoTemplate mongoTemplate;
    private static final String SERVICE_NAME = "serviceName";

    public void logRecord(ServiceTracker serviceTracker){
        mongoTemplate.save(serviceTracker);
    }

    public List<ServiceTracker> getServiceLogs(String serviceName){
        Query query = new Query(Criteria.where(SERVICE_NAME).is(serviceName));
        List<ServiceTracker> serviceTrackers = mongoTemplate.find(query,ServiceTracker.class);
        return serviceTrackers;
    }

    public List<ServiceTracker> getAllServiceTrackers(){
        return mongoTemplate.findAll(ServiceTracker.class);
    }
}

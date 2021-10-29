package com.vlad.discovery.service.dao;

import com.mongodb.client.result.DeleteResult;
import com.vlad.discovery.service.dto.Domain;
import com.vlad.discovery.service.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
public class DomainRepository {
    @Autowired
    MongoTemplate mongoTemplate;
    public static final String CLIENT_ID = "clientId";
    public static final String HASH = "*****";

    public Domain create(Domain domain){
        Query query = new Query(Criteria.where(CLIENT_ID).is(domain.getClientId()));
        try {
            mongoTemplate.save(domain);
            Domain createdDomain = mongoTemplate.findOne(query,Domain.class);
            createdDomain.setAppId(null);
            createdDomain.setClientSecret(HASH);
            return createdDomain;
        }catch (Exception e){
            log.error("An error occured while creating domain {}",e);
            return null;
        }
    }

    public Optional<Domain> getByDomainId(String domainId){
        Query query = new Query(Criteria.where(CLIENT_ID).is(domainId));
        Domain createdDomain = mongoTemplate.findOne(query,Domain.class);
        createdDomain.setAppId(null);
        createdDomain.setClientSecret(HASH);
        return Optional.ofNullable(createdDomain);
    }

    public void deleteDomain(String domainId){
        Query query = new Query(Criteria.where(CLIENT_ID).is(domainId));
        DeleteResult deleteResult = mongoTemplate.remove(query,Domain.class);
        if(!deleteResult.wasAcknowledged()){
            throw new IllegalArgumentException("record doesnt exist");
        }
    }

    public Domain updateDomain(Domain domain){
        Update update = new Update();
        update.set("lastUpdatedOn", LocalDateTime.now());
        if (!Utils.isNullorEmpty(domain.getClientSecret())) update.set("clientSecret",domain.getClientSecret());
        if (!Utils.isNullorEmpty(domain.getDomainName())) update.set("domainName",domain.getDomainName());
        Query query = new Query(Criteria.where(CLIENT_ID).is(domain.getClientId()));
        try {
            mongoTemplate.findAndModify(query,update,Domain.class);
            Domain updatedValue = mongoTemplate.findOne(query,Domain.class);
            updatedValue.setAppId(null);
            updatedValue.setClientSecret(HASH);
            return updatedValue;
        }catch (Exception e){
            log.error("An error occured while updating domain {}",e);
            return null;
        }
    }
}

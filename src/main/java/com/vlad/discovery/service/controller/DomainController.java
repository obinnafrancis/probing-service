package com.vlad.discovery.service.controller;

import com.vlad.discovery.service.model.BaseResponse;
import com.vlad.discovery.service.model.DomainModel;
import com.vlad.discovery.service.service.DomainService;
import com.vlad.discovery.service.service.impl.DomainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/domain")
public class DomainController {

    private DomainService domainService;

    @Autowired
    public DomainController(DomainServiceImpl domainService){
        this.domainService = domainService;
    }


    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> createService(@RequestBody @Validated DomainModel domainModel){
        return domainService.createDomain(domainModel);
    }

    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> updateService(@RequestBody @Validated DomainModel domainModel){
        domainService.updateDomain(domainModel);
        return ResponseEntity.ok(BaseResponse.builder().code("200").description("Successful").build());
    }

    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/{domainId}",produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> deleteService(@PathVariable(name = "domainId") String domainId){
        domainService.deleteDomain(domainId);
        return ResponseEntity.ok(BaseResponse.builder().code("200").description("Successful").build());
    }

    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{domainId}",produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> getService(@PathVariable(name = "domainId") String domainId){
        return domainService.getDomain(domainId);
    }

    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/services",produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> getDomainService(@RequestHeader(name = "domainId") String domainId){
        return domainService.getDomainServices(domainId);
    }
}

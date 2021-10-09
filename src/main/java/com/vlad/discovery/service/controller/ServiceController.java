package com.vlad.discovery.service.controller;

import com.vlad.discovery.service.model.BaseResponse;
import com.vlad.discovery.service.model.ServiceModel;
import com.vlad.discovery.service.service.ProbeService;
import com.vlad.discovery.service.service.impl.ProbeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
public class ServiceController {

    private ProbeService probeService;

    @Autowired
    public ServiceController(ProbeServiceImpl probeService){
        this.probeService = probeService;
    }


    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> createService(@RequestBody @Validated ServiceModel serviceModel){
        return probeService.createService(serviceModel);
    }

    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> updateService(@RequestBody @Validated ServiceModel serviceModel){
        probeService.updateService(serviceModel);
        return ResponseEntity.ok(BaseResponse.builder().code("200").description("Successful").build());
    }

    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/{name}",produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> deleteService(@PathVariable(name = "name") String serviceName){
        probeService.deleteService(serviceName);
        return ResponseEntity.ok(BaseResponse.builder().code("200").description("Successful").build());
    }

    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/{service-name}",produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> getService(@PathVariable(name = "service-name") String serviceName){
        return probeService.getService(serviceName);
    }
}

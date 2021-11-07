package com.vlad.discovery.service.controller;

import com.vlad.discovery.service.model.BaseResponse;
import com.vlad.discovery.service.model.ServiceModel;
import com.vlad.discovery.service.service.ProbeService;
import com.vlad.discovery.service.service.TrackerService;
import com.vlad.discovery.service.service.impl.ProbeServiceImpl;
import com.vlad.discovery.service.service.impl.TrackerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LogController {

    private TrackerService trackerService;

    @Autowired
    public LogController(TrackerServiceImpl trackerService){
        this.trackerService = trackerService;
    }


    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> getAll(){
        return trackerService.getAllLogs();
    }

    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{service}", consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<BaseResponse> getAll(@PathVariable (required = true, name = "service") String serviceName){
        return trackerService.getServiceLog(serviceName);
    }
}

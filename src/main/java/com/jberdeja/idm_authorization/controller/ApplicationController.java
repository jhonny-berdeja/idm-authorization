package com.jberdeja.idm_authorization.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jberdeja.idm_authorization.dto.http.ApplicactionRequest;
import com.jberdeja.idm_authorization.dto.http.ApplicationResponse;
import com.jberdeja.idm_authorization.dto.http.ApplicationsResponse;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.service.ApplicationService;

@RestController
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PostMapping(path = "/create-application")
    public ResponseEntity<?> createApplication(@Validated @RequestBody ApplicactionRequest request){
        ApplicationEntity applicationEntity = applicationService.createApplication(request.getApplicationEntity());
        return ResponseEntity.ok(new ApplicationResponse(applicationEntity));
    }

    @GetMapping(path = "/get-applications")
    public ResponseEntity<?> getApplications(){
        List<String> applications = applicationService.getApplications();
        return ResponseEntity.ok(new ApplicationsResponse(applications));
    }

    @GetMapping(path = "/get-application/{app}")
    public ResponseEntity<?> getApplication(@PathVariable String app){
        ApplicationEntity applicationEntity =  applicationService.getApplicationByName(app);
        return ResponseEntity.ok(new ApplicationResponse(applicationEntity));
    }
}

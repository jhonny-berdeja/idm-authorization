package com.jberdeja.idm_authorization.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.jberdeja.idm_authorization.dto.ApplicactionRequest;
import com.jberdeja.idm_authorization.dto.ApplicationResponse;
import com.jberdeja.idm_authorization.dto.ApplicationsResponse;
import com.jberdeja.idm_authorization.dto.AccessManagementRequest;
import com.jberdeja.idm_authorization.dto.AccessManagementResponse;
import com.jberdeja.idm_authorization.dto.UserIDMRequest;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.service.ApplicationService;
import com.jberdeja.idm_authorization.service.ManagementRecordService;
import com.jberdeja.idm_authorization.service.UserIdmService;

@RestController
public class Controller {
    @Autowired
    private UserIdmService userIDMService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ManagementRecordService managementRecordService;

    @GetMapping(path = "/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("OK");
    }

    @PostMapping(path = "/create-user")
    public ResponseEntity<String> createUser(@Validated @RequestBody UserIDMRequest request) {
        try{
            userIDMService.createUser(request);
            return ResponseEntity.ok("User created successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());// falta retornar bien el SERVER_ERROR
        }
    }

    @PostMapping(path = "/create-application")
    public ResponseEntity<?> createApplication(@Validated @RequestBody ApplicactionRequest request){
        try{
            ApplicationEntity applicationEntity = applicationService.createApplication(request.getApplicationEntity());
            return ResponseEntity.ok(new ApplicationResponse(applicationEntity));
            
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path = "/get-applications")
    public ResponseEntity<?> getApplications(){
        try{
            List<String> applications = applicationService.getApplications();
            return ResponseEntity.ok(new ApplicationsResponse(applications));
        }catch(Exception e){
            return null;
        }
    }

    @GetMapping(path = "/get-application/{app}")
    public ResponseEntity<?> getApplication(@PathVariable String app){
        try{
            ApplicationEntity applicationEntity =  applicationService.getApplicationByName(app);
            return ResponseEntity.ok(new ApplicationResponse(applicationEntity));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());// falta retornar bien el SERVER_ERROR
        }
    }

    @PostMapping(path = "/document-access-management")
    public ResponseEntity<?> documentAccessManagement(@Validated @RequestBody AccessManagementRequest request){
        try{
            var accessManagementDocumentationEntity = managementRecordService.register(request.getAccessManagement());
            return ResponseEntity.ok(new AccessManagementResponse(accessManagementDocumentationEntity));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
}

package com.jberdeja.idm_authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jberdeja.idm_authorization.dto.http.AccessManagementRequest;
import com.jberdeja.idm_authorization.dto.http.AccessManagementResponse;
import com.jberdeja.idm_authorization.service.ManagementRecordService;

@RestController
public class ManagementRecordController {
    @Autowired
    private ManagementRecordService managementRecordService;

    @PostMapping(path = "/document-access-management")
    public ResponseEntity<?> documentAccessManagement(@Validated @RequestBody AccessManagementRequest request){
        var managementRecordEntity = managementRecordService.register(request.getAccessManagement());
        return ResponseEntity.ok(new AccessManagementResponse(managementRecordEntity));
    }
}

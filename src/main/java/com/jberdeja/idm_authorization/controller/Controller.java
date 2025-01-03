package com.jberdeja.idm_authorization.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/* import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.jberdeja.idm_authorization.dto.common.UserIdm;
import com.jberdeja.idm_authorization.dto.http.AccessManagementRequest;
import com.jberdeja.idm_authorization.dto.http.AccessManagementResponse;
import com.jberdeja.idm_authorization.dto.http.ApplicactionRequest;
import com.jberdeja.idm_authorization.dto.http.ApplicationResponse;
import com.jberdeja.idm_authorization.dto.http.ApplicationsResponse;
import com.jberdeja.idm_authorization.dto.http.UserIdmRequest;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.service.ApplicationService;
import com.jberdeja.idm_authorization.service.ManagementRecordService;
import com.jberdeja.idm_authorization.service.UserIdmService; */

@RestController
public class Controller {
/*     @Autowired
    private UserIdmService userIdmService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ManagementRecordService managementRecordService;
 */
    @GetMapping(path = "/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("OK");
    }

/*     @PostMapping(path = "/create-user")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserIdmRequest request) {
        UserIdm userIdm = request.getUserIdm();
        userIdmService.createUser(userIdm);
        return ResponseEntity.ok("User created successfully");
    }

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

    @PostMapping(path = "/document-access-management")
    public ResponseEntity<?> documentAccessManagement(@Validated @RequestBody AccessManagementRequest request){
        var managementRecordEntity = managementRecordService.register(request.getAccessManagement());
        return ResponseEntity.ok(new AccessManagementResponse(managementRecordEntity));
    } */
    
}

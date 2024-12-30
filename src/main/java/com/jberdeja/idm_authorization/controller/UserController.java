package com.jberdeja.idm_authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jberdeja.idm_authorization.dto.common.UserIdm;
import com.jberdeja.idm_authorization.dto.http.UserIdmRequest;
import com.jberdeja.idm_authorization.service.UserIdmService;

@RestController
public class UserController {
    @Autowired
    private UserIdmService userIdmService;

    @PostMapping(path = "/create-user")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserIdmRequest request) {
        UserIdm userIdm = request.getUserIdm();
        userIdmService.createUser(userIdm);
        return ResponseEntity.ok("User created successfully");
    }
}

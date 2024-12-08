package com.jberdeja.idm_authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.jberdeja.idm_authorization.entityes.UserIDMRequest;
import com.jberdeja.idm_authorization.service.UserIDMService;
import jakarta.validation.Valid;

@RestController
public class Controller {
    @Autowired
    private UserIDMService userIDMService;

    @GetMapping(path = "/")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("OK");
    }

    @PostMapping(path = "/create-user")
    public ResponseEntity<String> handlePost(@Validated @RequestBody UserIDMRequest request) {
        try{
            userIDMService.createUser(request);
            return ResponseEntity.ok("User created successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

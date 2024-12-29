package com.jberdeja.idm_authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.dto.common.UserIdm;
import com.jberdeja.idm_authorization.processor.UserProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserIdmService {

    @Autowired
    private UserProcessor userProcessor;

    public void createUser(UserIdm userIdm ) {
        log.info("starting user creation");
        userProcessor.createUser(userIdm);
        log.info("user creation completed successfully");
    }

}

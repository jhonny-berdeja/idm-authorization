package com.jberdeja.idm_authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.dto.common.UserIdm;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;
import com.jberdeja.idm_authorization.executor.UserIdmRepositoryExecutor;
import com.jberdeja.idm_authorization.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserIdmService {
    @Autowired
    private UserIdmRepositoryExecutor userIdmRepositoryExecutor;
    @Autowired
    private UserMapper userMapper;

    public void createUser(UserIdm userIdm ) {
        log.info("starting user creation: " + userIdm.getEmail());
        UserIdmEntity userIdmEntity = mapToUserIdmEntity(userIdm);
        userIdmRepositoryExecutor.save(userIdmEntity);
        log.info("user created successfully");
    }

    private UserIdmEntity mapToUserIdmEntity(UserIdm userIdm){
        return userMapper.mapToUserIdmEntity(userIdm);
    }

}

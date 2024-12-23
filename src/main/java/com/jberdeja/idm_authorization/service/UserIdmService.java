package com.jberdeja.idm_authorization.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jberdeja.idm_authorization.dto.common.UserIdm;
import com.jberdeja.idm_authorization.dto.http.UserIdmRequest;
import com.jberdeja.idm_authorization.entity.RoleIdmEntity;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;
import com.jberdeja.idm_authorization.executor.UserIdmRepositoryExecutor;
import com.jberdeja.idm_authorization.mapper.UserMapper;
import com.jberdeja.idm_authorization.repository.RoleIdmRepository;
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
        UserIdmEntity userIdmEntity = userMapper.mapToUserIdmEntity(userIdm);
        userIdmRepositoryExecutor.save(userIdmEntity);
        log.info("user created successfully");
    }

}

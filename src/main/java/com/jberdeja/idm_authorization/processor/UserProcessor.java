package com.jberdeja.idm_authorization.processor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.entity.UserIdmEntity;
import com.jberdeja.idm_authorization.exception.PostgresSqlDatabaseIdmException;
import com.jberdeja.idm_authorization.executor.UserIdmRepositoryExecutor;
import com.jberdeja.idm_authorization.mapper.UserMapper;
import com.jberdeja.idm_authorization.validator.UserValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserProcessor {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserIdmRepositoryExecutor userIdmRepositoryExecutor;

    public Optional<User> mapToUser(Optional<UserIdmEntity> userIdmEntityOptional){
        return userMapper.mapToUser(userIdmEntityOptional);
    }

    public void validate(Optional<User> user){
        userValidator.validate(user);
    }

    public Optional<UserIdmEntity> findByEmail(String username){
        return userIdmRepositoryExecutor.findByEmail(username);
    }
}

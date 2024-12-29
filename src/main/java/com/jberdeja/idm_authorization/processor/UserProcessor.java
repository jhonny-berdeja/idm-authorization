package com.jberdeja.idm_authorization.processor;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.dto.common.UserIdm;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;
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

    public void createUser(UserIdm userIdm ) {
        validateUserExistence(userIdm);
        UserIdmEntity userIdmEntity = mapToUserIdmEntity(userIdm);
        userIdmRepositoryExecutor.save(userIdmEntity);
    }

    private void validateUserExistence(UserIdm userIdm ){
        userValidator.validateUserExistence(
            userIdm.getEmail(), 
            userIdmRepositoryExecutor::existsByEmail
        );
    }

    private UserIdmEntity mapToUserIdmEntity(UserIdm userIdm){
        return userMapper.mapToUserIdmEntity(userIdm);
    }


    public Optional<User> mapToUser(Optional<UserIdmEntity> userIdmEntityOptional){
        return userMapper.mapToUser(userIdmEntityOptional);
    }

    public void validateUserExistence(Optional<User> user){
        userValidator.validateUserExistence(user);
    }

    public Optional<UserIdmEntity> findByEmail(String username){
        return userIdmRepositoryExecutor.findByEmail(username);
    }
}

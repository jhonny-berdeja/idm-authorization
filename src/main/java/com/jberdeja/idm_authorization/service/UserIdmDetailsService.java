package com.jberdeja.idm_authorization.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.entity.RoleIdmEntity;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;
import com.jberdeja.idm_authorization.executor.UserIdmRepositoryExecutor;
import com.jberdeja.idm_authorization.mapper.UserMapper;
import com.jberdeja.idm_authorization.repository.UserIdmRepository;
import com.jberdeja.idm_authorization.validator.UserValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserIdmDetailsService implements UserDetailsService{
    @Autowired
    private UserIdmRepositoryExecutor userIdmRepositoryExecutor;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserValidator userValidator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserIdmEntity> userIdmEntity =  userIdmRepositoryExecutor.findByEmail(username);
        Optional<User> user = userMapper.mapToUser(userIdmEntity);
        userValidator.validate(user);
        return user.get();
    }
}

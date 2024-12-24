package com.jberdeja.idm_authorization.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;
import com.jberdeja.idm_authorization.processor.UserProcessor;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserIdmDetailsService implements UserDetailsService{

    @Autowired
    private UserProcessor userProcessor;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserIdmEntity> userIdmEntity =  userProcessor.findByEmail(username);
        Optional<User> user = userProcessor.mapToUser(userIdmEntity);
        userProcessor.validate(user);
        return user.get();
    }
}

package com.jberdeja.idm_authorization.validator;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public void validateUserExistence(Optional<User> user){
        user.orElseThrow(()->new UsernameNotFoundException("User not exist in database"));
    }
}

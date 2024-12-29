package com.jberdeja.idm_authorization.validator;

import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.utility.Utility;

@Component
public class UserValidator {
    public void validateUserExistence(Optional<User> user){
        user.orElseThrow(()->new UsernameNotFoundException("User not exist in database"));
    }

    public void validateUserExistence(String username, Predicate<String> validator){
        String errorMessage = "error, the user already existed";
        Utility.validate(
            username, 
            validator, 
            errorMessage
        );
    }
}

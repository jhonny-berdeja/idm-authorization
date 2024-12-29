package com.jberdeja.idm_authorization.validator;

import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.utility.Utility;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthorizationValidator {
    public void validateHttpServletRequest(HttpServletRequest request ){
        String errorMessage = "error, HttpServletRequest is null";
        Utility.validate(
            request, 
            Utility::isNullObject, 
            errorMessage
        );
    }
}

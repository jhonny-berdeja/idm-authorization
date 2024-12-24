package com.jberdeja.idm_authorization.validator;

import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ClaimsValidator {

    public void validateClaimsExpiration(Claims claims){
        if(claims.getExpiration().before(new Date())){
            log.error("error, the claims are from an expired token");
            throw new IllegalArgumentException("error, the claims are from an expired token");
        }
    }

    public void validateUsernamen(String username){
        if(isUsernameInvalid(username)){
            log.error("the username is not valid because it is null or blank");
            throw new IllegalArgumentException("the username is not valid because it is null or blank");
        }
    }
    
    private boolean isUsernameInvalid(String username){
        return ! isUsernameValid(username);
    }

    private boolean isUsernameValid(String username){
        return Objects.nonNull(username) && isNotBlackUsername(username);
    }

    private boolean isNotBlackUsername(String username){
        return ! username.isBlank();
    }
}

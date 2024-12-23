package com.jberdeja.idm_authorization.validator;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.dto.JwtValidatorResult;
import com.jberdeja.idm_authorization.processor.TokenProcessor;
import com.jberdeja.idm_authorization.service.UserIdmDetailsService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JWTValidator {
 @Autowired
    private UserIdmDetailsService userIDMDetailsService;
    @Autowired
    private TokenProcessor tokenProcessor;
    
    public JwtValidatorResult validateIfTokenIsOfAuthenticatedUser(String headerAuthorizationValue){
        try {
            Claims headerAuthorizationValueTokenClaims = tokenProcessor.getTokenClaimsFromAuthorizationHeaderValue(headerAuthorizationValue);
            String headerAuthorizationValueTokenClaimsUsername = tokenProcessor.getUsernameFromTokenClaims(headerAuthorizationValueTokenClaims);
            UserDetails userDetailsFromDatabase = getUserDetailsFromDatabaseByUsername(headerAuthorizationValueTokenClaimsUsername);
            validateIfTokenIsOfAuthenticatedUser(headerAuthorizationValueTokenClaimsUsername, userDetailsFromDatabase);
            return new JwtValidatorResult(userDetailsFromDatabase);
        } catch (RuntimeException e) {
            log.error("Eror validate token", e);
            throw new RuntimeException("Eror validate token", e);
        }
    }

    private UserDetails getUserDetailsFromDatabaseByUsername(String username){
        return userIDMDetailsService.loadUserByUsername(username);
    }
    
    private void validateIfTokenIsOfAuthenticatedUser(String headerAuthorizationValueTokenClaimsUsername, UserDetails userDetailsFromDatabase ){
        String usernameFromDatabase = userDetailsFromDatabase.getUsername();
        if(isNotTokenOwnedByAuthenticatedUser(headerAuthorizationValueTokenClaimsUsername, usernameFromDatabase)){
            log.error("the token is not the authenticated user because the user associated with the token does not match the authenticated user");
            throw new IllegalArgumentException("the token is not the authenticated user because the user associated with the token does not match the authenticated user");
        }
    }

    private boolean isNotTokenOwnedByAuthenticatedUser(String usernameFromToken, String usernameFromDatabase){
        return !isTokenOwnedByAuthenticatedUser(usernameFromToken, usernameFromDatabase);
    }

    private boolean isTokenOwnedByAuthenticatedUser(String usernameFromToken, String usernameFromDatabase){
        return usernameFromToken.equalsIgnoreCase(usernameFromDatabase);
    }

/*     public void validateUsernamen(String username){
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
    } */

}

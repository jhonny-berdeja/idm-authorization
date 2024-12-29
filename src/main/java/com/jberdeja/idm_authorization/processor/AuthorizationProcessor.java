package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.dto.validator_result.HeaderValidationResult;
import com.jberdeja.idm_authorization.dto.validator_result.JwtValidatorResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class AuthorizationProcessor implements Processor {
    @Autowired
    private HttpServletProcessor httpServletProcessor;
    @Autowired
    private SecurityContextHolderProcessor securityContextHolderProcessor;


    public HeaderValidationResult applyValidations(
        HttpServletRequest request
    ){

        String authorization = getHeaderAuthorization(request);
        var headerAuthorizationNotExists = headerAuthorizationNotExists(authorization);

        if(headerAuthorizationNotExists)
            return new HeaderValidationResult(Boolean.FALSE);
           
        var jwtResult = validateAuthorizationToken(authorization);
        var userDetails = jwtResult.getDatabaseUserDetails();

        validateContextAuthentication();
        setAuthenticationInCurrentThreadContext(userDetails, request);
        
        return new HeaderValidationResult(Boolean.TRUE);
    }

    private String getHeaderAuthorization(HttpServletRequest request) {
        return httpServletProcessor.getHeaderAuthorization(request);
    }
    
    private boolean headerAuthorizationNotExists(String authorization) {
        return httpServletProcessor.headerAuthorizationNotExists(authorization);
    }
    
    private JwtValidatorResult validateAuthorizationToken(String authorization) {
        return httpServletProcessor.validateTokenBelongsToAuthenticatedUser(
            authorization
        );
    }
    
    private void validateContextAuthentication() {
        securityContextHolderProcessor.verifyIfAuthenticationIsPresentInContext();
    }
    
    private void setAuthenticationInCurrentThreadContext(
        UserDetails userDetails, 
        HttpServletRequest request
    ) {
        
        securityContextHolderProcessor.setAuthenticationInContext(userDetails, request);
    }
    
}
package com.jberdeja.idm_authorization.processor;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.dto.validator_result.HeaderValidationResult;
import com.jberdeja.idm_authorization.dto.validator_result.JwtValidatorResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationProcessor implements Processor {
    @Autowired
    private HeaderProcessor headerProcessor;
    @Autowired
    private JwtProcessor jwtProcessor;
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

    private boolean headerAuthorizationNotExists(String authorization) {
        return headerProcessor.headerAuthorizationNotExists(authorization);
    }
    
    private JwtValidatorResult validateAuthorizationToken(String authorization) {
        return jwtProcessor.validateTokenBelongsToAuthenticatedUser(
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

    private String getHeaderAuthorization(HttpServletRequest request){
        return headerProcessor.getHeaderAuthorization(request);
    }

    public boolean applyAuthorizationValidation(
        HttpServletRequest request, 
        HttpServletResponse response,
        Processor authorizationProcessor
    ) throws IOException{

        return headerProcessor.applyAuthorizationValidation(
            request, 
            response, 
            authorizationProcessor
        );
    }


    
}
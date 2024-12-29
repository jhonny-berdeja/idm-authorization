package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.dto.validator_result.JwtValidatorResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorizationFilterProcessor {
    @Autowired
    private HttpServletProcessor httpServletProcessor;
    @Autowired
    private SecurityContextHolderProcessor securityContextHolderProcessor;
    @Autowired
    private JwtProcessor jwtProcessor;

    public String obtainRequestURL(HttpServletRequest request) {
        return httpServletProcessor.obtainRequestURL(request);
    }

    public String getHeaderAuthorization(HttpServletRequest request) {
        return httpServletProcessor.getHeaderAuthorization(request);
    }
    
    public boolean headerAuthorizationExists(String authHeader){
        return httpServletProcessor.headerAuthorizationExists(authHeader);
    }
    
    public JwtValidatorResult validateAuthorizationToken(String authorization) {
        return jwtProcessor.validateTokenBelongsToAuthenticatedUser(authorization);
    }
    
    public void validateContextAuthentication() {
        securityContextHolderProcessor.verifyIfAuthenticationIsPresentInContext();
    }
    
    public void setAuthenticationInCurrentThreadContext(
        UserDetails userDetails, 
        HttpServletRequest request
    ) {
        
        securityContextHolderProcessor.setAuthenticationInContext(userDetails, request);
    }
}

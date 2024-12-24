package com.jberdeja.idm_authorization.security.filter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jberdeja.idm_authorization.dto.validator_result.JwtValidatorResult;
import com.jberdeja.idm_authorization.processor.HttpServletRequestProcessor;
import com.jberdeja.idm_authorization.processor.JwtProcessor;
import com.jberdeja.idm_authorization.processor.SecurityContextHolderProcessor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter{

    @Autowired
    private HttpServletRequestProcessor httpServletRequestProcessor;
    @Autowired
    private SecurityContextHolderProcessor securityContextHolderProcessor;
    @Autowired
    private JwtProcessor jwtProcessor;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
        ) throws ServletException, IOException {

        String url = obtainRequestURL(request);

        if (isValidationRequired(url)) {
            applyValidations(request);
        }

        filterChain.doFilter(request, response);
    }

    private String obtainRequestURL(HttpServletRequest request) {
        return httpServletRequestProcessor.obtainRequestURL(request);
    }

    private void applyValidations(HttpServletRequest request) {
        String authorization = getHeaderAuthorization(request);
        validateAuthorizationHeader(authorization);
        
        JwtValidatorResult jwtResult = validateToken(authorization);
        UserDetails userDetails = jwtResult.getDatabaseUserDetails();
        
        validateSecurityContext();
        setAuthenticationToContext(userDetails, request);
    }

    private String getHeaderAuthorization(HttpServletRequest request) {
        return httpServletRequestProcessor.getHeaderAuthorization(request);
    }
    
    private void validateAuthorizationHeader(String authHeader) {
        httpServletRequestProcessor.validateHeaderAuthorization(authHeader);
    }
    
    private JwtValidatorResult validateToken(String authorization) {
        return jwtProcessor.validateTokenBelongsToAuthenticatedUser(authorization);
    }
    
    private void validateSecurityContext() {
        securityContextHolderProcessor.validateAuthenticationInCurrentThreadContext();
    }
    
    private void setAuthenticationToContext(UserDetails userDetails, HttpServletRequest request) {
        securityContextHolderProcessor.addAuthenticationToTheCurrentThreadContext(userDetails, request);
    }
    
    private boolean isValidationRequired(String requestURL){
        return httpServletRequestProcessor.isValidationRequired(requestURL);
    }
}
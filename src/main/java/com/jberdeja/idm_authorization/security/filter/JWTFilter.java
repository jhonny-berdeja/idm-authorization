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
        if (isValidationRequired(url)){
            applyValidations(request);
        }
        filterChain.doFilter(request, response);
    }

    private String obtainRequestURL(HttpServletRequest request) {
        return httpServletRequestProcessor.obtainRequestURL(request);
    }

    private void applyValidations(HttpServletRequest request) {
        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        String authorization = getHeaderAuthorization(request);
        validateAuthorization(authorization);
        
        JwtValidatorResult jwtResult = validateAuthorizationToken(authorization);
        UserDetails userDetails = jwtResult.getDatabaseUserDetails();
        
        validateAuthentication();
        setAuthenticationInCurrentThreadContext(userDetails, request);
    }

    private String getHeaderAuthorization(HttpServletRequest request) {
        return httpServletRequestProcessor.getHeaderAuthorization(request);
    }
    
    private void validateAuthorization(String authHeader) {
        httpServletRequestProcessor.validateAuthorizationExistence(authHeader);
    }
    
    private JwtValidatorResult validateAuthorizationToken(String authorization) {
        return jwtProcessor.validateTokenBelongsToAuthenticatedUser(authorization);
    }
    
    private void validateAuthentication() {
        securityContextHolderProcessor.verifyIfAuthenticationIsPresentInContext();
    }
    
    private void setAuthenticationInCurrentThreadContext(UserDetails userDetails, HttpServletRequest request) {
        securityContextHolderProcessor.setAuthenticationInContext(userDetails, request);
    }
    
    private boolean isValidationRequired(String requestURL){
        return httpServletRequestProcessor.isValidationRequired(requestURL);
    }
}
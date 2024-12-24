package com.jberdeja.idm_authorization.processor;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.jberdeja.idm_authorization.validator.SecurityContextHolderValidator;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecurityContextHolderProcessor {
    private static final Object OBJECT_NULL = null;

    @Autowired
    private SecurityContextHolderValidator securityContextHolderValidator;

    public void validateAuthenticationInCurrentThreadContext(){
        securityContextHolderValidator.validateAuthenticationInCurrentThreadContext();
    }

    public void addAuthenticationToTheCurrentThreadContext(UserDetails userDetailsOfDatabase, HttpServletRequest request){
        var usernamePasswordAuthenticationToken = buildUsernamePasswordAuthenticationToken(userDetailsOfDatabase, request);
        addAuthenticationToTheCurrentThreadContext(usernamePasswordAuthenticationToken);
    }

    private void addAuthenticationToTheCurrentThreadContext(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
        try{
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }catch(Exception e){
            log. error("error when adding authentication to the current thread context", e);
            throw new RuntimeException("error when adding authentication to the current thread context", e);
        }
    }

    private UsernamePasswordAuthenticationToken buildUsernamePasswordAuthenticationToken(UserDetails userDetailsOfDatabase, HttpServletRequest request){
        
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetailsOfDatabase
            , OBJECT_NULL
            , userDetailsOfDatabase.getAuthorities());
            WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
            usernamePasswordAuthenticationToken.setDetails(webAuthenticationDetails
        );

        return usernamePasswordAuthenticationToken;
    }
}

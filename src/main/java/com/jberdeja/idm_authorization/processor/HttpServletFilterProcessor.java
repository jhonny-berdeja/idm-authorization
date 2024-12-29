package com.jberdeja.idm_authorization.processor;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpServletFilterProcessor {


    @Autowired
    private AuthorizationProcessor authorizationProcessor;
    @Autowired
    private XxsrfTokenProcessor xxsrfTokenProcessor;
    @Autowired
    private CookieProcessor cookieProcessor;
    @Autowired
    private CsrfTokenProcessor csrfTokenProcessor;

    public boolean isNecessaryToFilter(
        boolean authorization, 
        boolean xXsrfToken, 
        boolean cookie,
        boolean csrfToken
    ){

        return authorization && xXsrfToken && cookie && csrfToken;
    }


    public boolean applyCsrfTokenValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return csrfTokenProcessor.applyCsrfTokenValidation(
            request, 
            response,
            csrfTokenProcessor
        );
    }


    public boolean applyAuthorizationValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return authorizationProcessor.applyAuthorizationValidation(
            request, 
            response, 
            authorizationProcessor
        );
    }

    public boolean applyCookieValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return cookieProcessor.applyCookieValidation(
            request, 
            response, 
            cookieProcessor);
    }

    public boolean applyXxsrfTokenValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return xxsrfTokenProcessor.applyXxsrfTokenValidation(
            request, 
            response, 
            xxsrfTokenProcessor
        );
    }
}

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
    private HttpServletProcessor httpServletProcessor;
    @Autowired 
    private AuthorizationProcessor authorizationProcessor;
    @Autowired
    private XxsrfTokenProcessor xxsrfTokenProcessor;
    @Autowired
    private CookieProcessor cookieProcessor;
    @Autowired
    private CsrfCookieProcessor csrfCookieProcessor;

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

        boolean validationResult = applyValidation(
            csrfCookieProcessor, 
            request, 
            response, 
            "error, csrf token attribite is null"
        );

        if(isResultInvalid(validationResult)){
            return Boolean.FALSE;
        }

        var csrfToken = httpServletProcessor.geAttributeCsrfToken(request);
        httpServletProcessor.addCsrfTokenForNextRequest(response, csrfToken);
        return Boolean.TRUE;
    }

    public boolean applyAuthorizationValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return applyValidation(
            authorizationProcessor, 
            request, 
            response, 
            "error, authorization header is null or blank"
        );
    }

    public boolean applyXxsrfTokenValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return applyValidation(
            xxsrfTokenProcessor, 
            request, 
            response, 
            "error, x-xsrf-token header is null or blank"
        );
    }

    public boolean applyCookieValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return applyValidation(
            cookieProcessor, 
            request, 
            response, 
            "error, cookie header is null or blank"
        );
    }

    private boolean applyValidation(
        Processor processor, 
        HttpServletRequest request, 
        HttpServletResponse response, 
        String erreMessage
    ) throws IOException{

        var authorization = processor.applyValidations(request);
        if(authorization.isNecessaryToFilter()){
            return Boolean.TRUE;
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
        response.getWriter().write(erreMessage);
        return Boolean.FALSE;
    }

    private boolean isResultInvalid(boolean validationResult){
        return ! validationResult;
    }
}

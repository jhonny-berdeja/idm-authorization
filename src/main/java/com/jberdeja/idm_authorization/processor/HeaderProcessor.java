package com.jberdeja.idm_authorization.processor;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.validator.HeaderValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HeaderProcessor {
    private static final String PREFIX_BEARER = "(?i)Bearer ";
    private static final String EMPTY = "";
    private static final String HEADER_COOKIE = "Cookie";
    private static final String HEADER_X_XSRF_TOKEN = "X-XSRF-TOKEN";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    @Autowired
    private HeaderValidator headerValidator;

    public boolean applyAuthorizationValidation(
        HttpServletRequest request, 
        HttpServletResponse response,
        Processor authorizationProcessor
    ) throws IOException{

        return headerValidator.applyAuthorizationValidation(
            request, 
            response, 
            authorizationProcessor
        );
    }

    public boolean applyXxsrfTokenValidation(
        HttpServletRequest request, 
        HttpServletResponse response,
        Processor xxsrfTokenProcessor
    ) throws IOException{

        return headerValidator.applyXxsrfTokenValidation(
            request, 
            response, 
            xxsrfTokenProcessor
        );
    }

    public boolean applyCookieValidation(
        HttpServletRequest request, 
        HttpServletResponse response,
        Processor cookieProcessor
    ) throws IOException{

        return headerValidator.applyXxsrfTokenValidation(
            request, 
            response, 
            cookieProcessor
        );
    }



    public boolean headerXxsrfTokenExists(String xXsrfToken){
        return headerValidator.headerXxsrfTokenExists(xXsrfToken);
    }



    public boolean headerAuthorizationExists(String authorization){
        return headerValidator.headerAuthorizationExists(authorization);
    }

    public String getAuthorizationToken(String headerAuthorization){
        headerValidator.validateAuthorizationContent(headerAuthorization);
        return headerAuthorization.replaceFirst(PREFIX_BEARER, EMPTY);
    }

    public void validateCsrfToken(CsrfToken csrfToken){
        headerValidator.validateCsrfToken(csrfToken);
    }





    public String getHeaderCookie(HttpServletRequest request){
        headerValidator.validateHttpServletRequest(request);
        return request.getHeader(HEADER_COOKIE); 
    }

    public boolean headerCookieExists(String cookie){
        return headerValidator.headerCookieExists(cookie);
    }

    public String getHeaderXxsrfToken(HttpServletRequest request){
        headerValidator.validateHttpServletRequest(request);
        return request.getHeader(HEADER_X_XSRF_TOKEN);
    }

    public String getHeaderAuthorization(HttpServletRequest request){
        headerValidator.validateHttpServletRequest(request);
        return request.getHeader(HEADER_AUTHORIZATION); 
    }

    public boolean headerAuthorizationNotExists(String authorization) {
        return headerValidator.headerAuthorizationNotExists(authorization);
    }

}

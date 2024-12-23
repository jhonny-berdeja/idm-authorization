package com.jberdeja.idm_authorization.processor;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HttpServletRequestProcessor {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public String getHeaderAuthorizationValue(HttpServletRequest request){
        try{
            return request.getHeader(AUTHORIZATION_HEADER); 
        }catch(Exception e){
            log.error("error, getting authorization value from header", e);
            throw new IllegalArgumentException("error, getting authorization value from header", e);
        }
    }

    public String obtainRequestURL(HttpServletRequest request){
        try{
        return request.getRequestURL().toString();
        }catch(Exception e){
            log.error("error when obtaining the url with which the request was made", e);
            throw new IllegalArgumentException("error when obtaining the url with which the request was made", e);
        }
    }

    public CsrfToken getCsrfTokenFromHeader(HttpServletRequest request){
        try{
            return (CsrfToken)request.getAttribute(CsrfToken.class.getName());   
        }catch(Exception e){
            log.error("error getting CsrfToken from header", e);
            throw new IllegalArgumentException("error getting CsrfToken from header", e);
        }
    }

    public void addCsrfTokenToResponseHeaderForNextRequest(HttpServletResponse response, CsrfToken csrfToken){
        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
    }

}

package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HttpServletRequestProcessor {
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String URL_HELLO = "http://localhost/hello";

    @Autowired
    private HeaderProcessor headerProcessor;

    public String getHeaderAuthorization(HttpServletRequest request){
        try{
            return request.getHeader(HEADER_AUTHORIZATION); 
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

    public void addCsrfTokenToResponseHeaderForNextRequest(HttpServletResponse response, CsrfToken csrfToken){
        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
    }

    public CsrfToken getHeaderCsrfToken(HttpServletRequest request){
        try{
            return (CsrfToken)request.getAttribute(CsrfToken.class.getName());   
        }catch(Exception e){
            log.error("error getting CsrfToken from header", e);
            throw new IllegalArgumentException("error getting CsrfToken from header", e);
        }
    }

    public void validateHeaderAuthorization(String authHeader) {
        headerProcessor.validateHeaderAuthorization(authHeader);
    }

    public boolean isValidationRequired(String requestURL){
        return ! isNotValidationRequired(requestURL);
    }

    private boolean isNotValidationRequired(String url){
        return isLocalOrDevelopmentEnvironment() && isURLFree(url);
    }

    private boolean isLocalOrDevelopmentEnvironment(){
        // TODO Implementar este metodo
        // TODO Implementar este metodo
        // TODO Implementar este metodo
        // TODO Implementar este metodo
        // TODO Implementar este metodo
        return Boolean.TRUE;
    }

    private boolean isURLFree(String url){
        return URL_HELLO.equalsIgnoreCase(url);
    }

}

package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.validator.HttpServletRequestValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HttpServletRequestProcessor {
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String URL_HELLO = "http://localhost/hello";
    @Autowired
    private HttpServletRequestValidator httpServletRequestValidator;
    @Autowired
    private HeaderProcessor headerProcessor;

    public String getHeaderAuthorization(HttpServletRequest request){
        validateHttpServletRequest(request);
        return request.getHeader(HEADER_AUTHORIZATION); 
    }

    public String obtainRequestURL(HttpServletRequest request){
        validateHttpServletRequest(request);
        return request.getRequestURL().toString();
    }

    public void addCsrfTokenToResponse(
        HttpServletResponse response, 
        CsrfToken csrfToken
    ){
        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
    }

    public CsrfToken getHeaderCsrfToken(HttpServletRequest request){
        validateHttpServletRequest(request);
        CsrfToken csrfToken = getCsrfToken(request);
        validateCsrfToken(csrfToken);
        return csrfToken;
    }

    private CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken)request.getAttribute(CsrfToken.class.getName());  
    }

    public void validateAuthorizationExistence(String authHeader) {
        headerProcessor.validateAuthorizationExistence(authHeader);
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

    private void validateHttpServletRequest(HttpServletRequest request){
        httpServletRequestValidator.validateHttpServletRequest(request);
    }

    private void validateCsrfToken(CsrfToken csrfToken){
        httpServletRequestValidator.validateCsrfToken(csrfToken);
    }

}

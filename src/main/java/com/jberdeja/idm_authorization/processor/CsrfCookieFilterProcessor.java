package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CsrfCookieFilterProcessor {
    @Autowired
    private HttpServletProcessor httpServletRequestProcessor;
    @Autowired
    private HeaderProcessor headerProcessor;

    public CsrfToken geAttributeCsrfToken(HttpServletRequest request){
        return httpServletRequestProcessor.geAttributeCsrfToken(request);   
    }

    public void validateCsrfToken(CsrfToken csrfToken ){
        headerProcessor.validateCsrfToken(csrfToken);
    }
}

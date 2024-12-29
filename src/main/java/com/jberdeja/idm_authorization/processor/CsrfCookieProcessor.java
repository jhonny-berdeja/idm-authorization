package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.dto.validator_result.HeaderValidationResult;
import com.jberdeja.idm_authorization.validator.CsrfCookieValidator;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CsrfCookieProcessor implements Processor{
    @Autowired
    private HttpServletProcessor httpServletProcessor;
    @Autowired
    private CsrfCookieValidator csrfCookieValidator;

    @Override
    public HeaderValidationResult applyValidations(HttpServletRequest request) {
        CsrfToken csrfToken = geAttributeCsrfToken(request);                    
        if(attributeCsrfTokenExists(csrfToken)){
            return new HeaderValidationResult(Boolean.TRUE);
        } 
        return new HeaderValidationResult(Boolean.FALSE);
    }
    
    public CsrfToken geAttributeCsrfToken(HttpServletRequest request){
        return httpServletProcessor.geAttributeCsrfToken(request);   
    }

    private boolean attributeCsrfTokenExists(CsrfToken csrfToken ){
        return csrfCookieValidator.attributeCsrfTokenExists(csrfToken);
    }

}

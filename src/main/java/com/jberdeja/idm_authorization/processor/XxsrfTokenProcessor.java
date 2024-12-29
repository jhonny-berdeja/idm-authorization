package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.dto.validator_result.HeaderValidationResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class XxsrfTokenProcessor implements Processor {

    @Autowired
    private HttpServletProcessor httpServletProcessor;

    @Override
    public HeaderValidationResult applyValidations(HttpServletRequest request){
        String xXsrfToken = getHeaderXxsrfToken(request);
        if(headerXxsrfTokenExists(xXsrfToken)){
            return new HeaderValidationResult(Boolean.TRUE);
        }
        return new HeaderValidationResult(Boolean.FALSE);
    }

    private String getHeaderXxsrfToken(HttpServletRequest request){
        return httpServletProcessor.getHeaderXxsrfToken(request);
    }

    private boolean headerXxsrfTokenExists(String xXsrfToken){
        return httpServletProcessor.headerXxsrfTokenExists(xXsrfToken);
    }

}

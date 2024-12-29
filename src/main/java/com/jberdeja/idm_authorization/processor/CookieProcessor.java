package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.dto.validator_result.HeaderValidationResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CookieProcessor implements Processor{
    @Autowired
    private HttpServletProcessor httpServletProcessor;

    @Override
    public HeaderValidationResult applyValidations(HttpServletRequest request) {
        String cookie = getHeaderCookie(request);
        if(headerCookieExists(cookie)){
            return new HeaderValidationResult(Boolean.TRUE);
        }
        return new HeaderValidationResult(Boolean.FALSE);
    }

    private String getHeaderCookie(HttpServletRequest request){
        return httpServletProcessor.getHeaderCookie(request);
    }

    private boolean headerCookieExists(String cookie){
        return httpServletProcessor.headerCookieExists(cookie);
    }

}

package com.jberdeja.idm_authorization.processor;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.validator.AttributeValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AttributeProcessor {



    @Autowired
    private AttributeValidator attributeValidator;

    public boolean applyCsrfTokenValidation(
        HttpServletRequest request, 
        HttpServletResponse response,
        Function<HttpServletRequest, CsrfToken> geAttributeCsrfToken,
        BiConsumer<HttpServletResponse, CsrfToken> addCsrfTokenForNextRequest,
        Processor csrfCookieProcessor
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

        var csrfToken = geAttributeCsrfToken.apply(request);
        addCsrfTokenForNextRequest.accept(response, csrfToken);
        return Boolean.TRUE;
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

     public CsrfToken geAttributeCsrfToken(HttpServletRequest request){
        validateHttpServletRequest(request);
        CsrfToken csrfToken = getCsrfToken(request);
        validateCsrfToken(csrfToken);
        return csrfToken;
    } 

    private CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken)request.getAttribute(CsrfToken.class.getName());  
    }

    private void validateHttpServletRequest(HttpServletRequest request){
        attributeValidator.validateHttpServletRequest(request);
    }

    private void validateCsrfToken(CsrfToken csrfToken){
        attributeValidator.validateCsrfToken(csrfToken);
    }  

    public boolean attributeCsrfTokenExists(CsrfToken csrfToken){
        return attributeValidator.attributeCsrfTokenExists(csrfToken);
    }
}

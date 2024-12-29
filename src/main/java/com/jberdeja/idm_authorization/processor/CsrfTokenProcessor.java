package com.jberdeja.idm_authorization.processor;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.dto.validator_result.HeaderValidationResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CsrfTokenProcessor implements Processor{
    @Autowired
    private AttributeProcessor attributeProcessor;

    @Override
    public HeaderValidationResult applyValidations(HttpServletRequest request) {
        CsrfToken csrfToken = geAttributeCsrfToken(request);                    
        if(attributeCsrfTokenExists(csrfToken)){
            return new HeaderValidationResult(Boolean.TRUE);
        } 
        return new HeaderValidationResult(Boolean.FALSE);
    }
    
    public CsrfToken geAttributeCsrfToken(HttpServletRequest request){
        return attributeProcessor.geAttributeCsrfToken(request);   
    }

    private boolean attributeCsrfTokenExists(CsrfToken csrfToken ){
        return attributeProcessor.attributeCsrfTokenExists(csrfToken);
    }

    public boolean applyCsrfTokenValidation(
        HttpServletRequest request, 
        HttpServletResponse response,
        Processor csrfTokenProcessor
    ) throws IOException{

        return attributeProcessor.applyCsrfTokenValidation(
            request, 
            response,
            this::geAttributeCsrfToken,
            this::addCsrfTokenForNextRequest,
            csrfTokenProcessor
        );
    }
    public void addCsrfTokenForNextRequest(
        HttpServletResponse response, 
        CsrfToken csrfToken
    ){
        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
    }
    
}

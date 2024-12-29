package com.jberdeja.idm_authorization.validator;

import java.io.IOException;
import java.util.Objects;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.processor.Processor;
import com.jberdeja.idm_authorization.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HeaderValidator {
    private static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";
    private static final String HEADER_COOKIE = "Cookie";

    public boolean applyAuthorizationValidation(
        HttpServletRequest request, 
        HttpServletResponse response,
        Processor authorizationProcessor
    ) throws IOException{

        return applyValidation(
            authorizationProcessor, 
            request, 
            response, 
            "error, authorization header is null or blank"
        );
    }

    public boolean applyXxsrfTokenValidation(
        HttpServletRequest request, 
        HttpServletResponse response,
        Processor xxsrfTokenProcessor
    ) throws IOException{

        return applyValidation(
            xxsrfTokenProcessor, 
            request, 
            response, 
            "error, x-xsrf-token header is null or blank"
        );
    }

    public boolean applyCookieValidation(
        HttpServletRequest request, 
        HttpServletResponse response,
        Processor cookieProcessor
    ) throws IOException{

        return applyValidation(
            cookieProcessor, 
            request, 
            response, 
            "error, cookie header is null or blank"
        );
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

    public boolean headerAuthorizationNotExists(String authorization) {
        return Utility.isNullOrBlank(authorization);
    }


    public boolean headerCookieExists(String cookie){
        return Utility.isNotNullOrBlank(cookie);
    }

    public boolean headerXxsrfTokenExists(String xXsrfToken){
        return Utility.isNotNullOrBlank(xXsrfToken);
    }

    public boolean headerAuthorizationExists(String authorization){
        return Utility.isNotNullOrBlank(authorization);
    }

    public void validateAuthorizationContent(String authorization){
        validateIfAuthorizationIsNullOrBlank(authorization);
        validateIfAuthorizationStartsWithBearer(authorization);
    }

    public void validateCsrfToken(CsrfToken csrfToken){
        String errorMessage = "error, CsrfToken is null";
        Utility.validate(
            csrfToken, 
            this::isCsrfTokenInvalid, 
            errorMessage
        );
    }

    private boolean isCsrfTokenInvalid(CsrfToken csrfToken){
        return ! isCsrfTokenValid(csrfToken);
    }
    private boolean isCsrfTokenValid(CsrfToken csrfToken){
        return Objects.nonNull(csrfToken.getHeaderName());
    }

    private void validateIfAuthorizationStartsWithBearer(String authorization){
        String errorMessage = "error, authorization content does not start with 'Bearer'";
        Utility.validate(
            authorization, 
            this::authorizationDoesnNotStartWithBearer, 
            errorMessage
        );
    }

    private boolean authorizationDoesnNotStartWithBearer(String authorization){
        return !authorizationStartWithBearer(authorization);
    }

    private boolean authorizationStartWithBearer(String authorization){
        return authorization.startsWith(AUTHORIZATION_HEADER_BEARER);
    }

    private void validateIfAuthorizationIsNullOrBlank(String authorization){
        String errorMessage = "error, authorization header is null or blank";
        Utility.validate(
            authorization, 
            Utility::isNullObject, 
            errorMessage
        );
    }









    public String getHeaderCookie(HttpServletRequest request){
        validateHttpServletRequest(request);
        return request.getHeader(HEADER_COOKIE); 
    }

    public void validateHttpServletRequest(HttpServletRequest request ){
        Utility.validate(
            request, 
            Utility::isNullObject, 
            "error, HttpServletRequest is null"
        );
    }

}

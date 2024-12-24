package com.jberdeja.idm_authorization.validator;

import java.util.Objects;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HeaderValidator {
    private static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

    public void validateHeaderAuthorizationValue(String headerAuthorizationValue){
        if(isHeaderAuthorizationValueInvalid(headerAuthorizationValue)){
            log.error("error, header authorization value is null or blank");
            throw new IllegalArgumentException("error, header authorization value is null or blank");
        }
    }

    public void validateHeaderAuthorization(String authorization){
        if(isHeaderAuthorizationInvalid(authorization))
            throw new IllegalArgumentException("The authorization header value in the request is invalid");
    }

    public void validateCsrfToken(CsrfToken csrfToken){
        if(isCsrfTokenInvalid(csrfToken)){
            log.error("error, CsrfToken is not in the request header");
            throw new IllegalArgumentException("error, CsrfToken is not in the request header");
        }

    }

    private boolean isCsrfTokenInvalid(CsrfToken csrfToken){
        return ! isCsrfTokenValid(csrfToken);
    }
    private boolean isCsrfTokenValid(CsrfToken csrfToken){
        return Objects.nonNull(csrfToken.getHeaderName());
    }

    private boolean isHeaderAuthorizationValueInvalid(String headerAuthorizationValue){
        return ! isHeaderAuthorizationValueValid(headerAuthorizationValue);
    }

    private boolean isHeaderAuthorizationValueValid(String headerAuthorizationValue){
        return Objects.nonNull(headerAuthorizationValue) && isNotBlackHeaderAuthorizationValue(headerAuthorizationValue);
    }

    private boolean isNotBlackHeaderAuthorizationValue(String headerAuthorizationValue){
        return ! headerAuthorizationValue.isBlank();
    }

    private boolean isHeaderAuthorizationInvalid(String authorizationHeader){
        return !isHeaderAuthorizationValid(authorizationHeader);
    }

    private boolean isHeaderAuthorizationValid(String requestTokenHeader){
        return Objects.nonNull(requestTokenHeader) 
                    && requestTokenHeader.startsWith(AUTHORIZATION_HEADER_BEARER);
    }
}

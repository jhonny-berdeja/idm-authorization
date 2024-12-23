package com.jberdeja.idm_authorization.processor;

import java.util.Objects;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HeaderProcessor {
    private static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";
    private static final String PREFIX_BEARER = "(?i)Bearer ";
    private static final String EMPTY = "";

    public void validateHeaderAuthorizationValue(String headerAuthorizationValue){
        if(isHeaderAuthorizationValueInvalid(headerAuthorizationValue)){
            log.error("error, header authorization value is null or blank");
            throw new IllegalArgumentException("error, header authorization value is null or blank");
        }
    }

    public String getAuthorizationValueTokenFromRequestHeader(String headerAuthorizationValue){
        if(isAuthorizationHeaderValueInvalid(headerAuthorizationValue))
            throw new IllegalArgumentException("The authorization header value in the request is invalid");
        return headerAuthorizationValue.replaceFirst(PREFIX_BEARER, EMPTY);
    }

    public void validateHeaderCsrfToken(CsrfToken csrfToken){
        if(isHeaderCsrfTokenInvalid(csrfToken)){
            log.error("error, CsrfToken is not in the request header");
            throw new IllegalArgumentException("error, CsrfToken is not in the request header");
        }

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

    private boolean isAuthorizationHeaderValueInvalid(String authorizationHeader){
        return !isAuthorizationHeaderValueValid(authorizationHeader);
    }
    private boolean isAuthorizationHeaderValueValid(String requestTokenHeader){
        return Objects.nonNull(requestTokenHeader) 
                    && requestTokenHeader.startsWith(AUTHORIZATION_HEADER_BEARER);
    }



    private boolean isHeaderCsrfTokenInvalid(CsrfToken csrfToken){
        return ! isHeaderCsrfTokenValid(csrfToken);
    }
    private boolean isHeaderCsrfTokenValid(CsrfToken csrfToken){
        return Objects.nonNull(csrfToken.getHeaderName());
    }
}

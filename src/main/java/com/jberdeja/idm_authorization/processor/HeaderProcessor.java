package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.validator.HeaderValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HeaderProcessor {
    private static final String PREFIX_BEARER = "(?i)Bearer ";
    private static final String EMPTY = "";

    @Autowired
    private HeaderValidator headerValidator;

    public boolean headerAuthorizationNotExists(String authorization) {
        return headerValidator.headerAuthorizationNotExists(authorization);
    }

    public boolean headerXxsrfTokenExists(String xXsrfToken){
        return headerValidator.headerXxsrfTokenExists(xXsrfToken);
    }

    public boolean headerCookieExists(String cookie){
        return headerValidator.headerCookieExists(cookie);
    }

    public boolean headerAuthorizationExists(String authorization){
        return headerValidator.headerAuthorizationExists(authorization);
    }

    public String getAuthorizationToken(String headerAuthorization){
        headerValidator.validateAuthorizationContent(headerAuthorization);
        return headerAuthorization.replaceFirst(PREFIX_BEARER, EMPTY);
    }

    public void validateCsrfToken(CsrfToken csrfToken){
        headerValidator.validateCsrfToken(csrfToken);
    }
}

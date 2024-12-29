package com.jberdeja.idm_authorization.validator;

import java.util.Objects;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.utility.Utility;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AttributeValidator {
    public void validateHttpServletRequest(HttpServletRequest request ){
        Utility.validate(
            request, 
            Utility::isNullObject, 
            "error, HttpServletRequest is null"
        );
    }

    public void validateCsrfToken(CsrfToken csrfToken){
        String errorMessage = "error, CsrfToken is null";
        Utility.validate(
            csrfToken, 
            Utility::isNullObject, 
            errorMessage
        );
    }

    public boolean attributeCsrfTokenExists(CsrfToken csrfToken){
        return Objects.nonNull(csrfToken.getHeaderName());
    }

}

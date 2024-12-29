package com.jberdeja.idm_authorization.validator;

import org.springframework.stereotype.Component;
import org.springframework.security.web.csrf.CsrfToken;
import com.jberdeja.idm_authorization.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpServletValidator {

    public boolean isNecessaryToFilter(
        boolean authorization, 
        boolean xXsrfToken, 
        boolean cookie,
        boolean csrfToken
    ){

        return authorization && xXsrfToken && cookie && csrfToken;
    }

    public void validateHttpServletRequest(HttpServletRequest request ){
        String errorMessage = "error, HttpServletRequest is null";
        Utility.validate(
            request, 
            Utility::isNullObject, 
            errorMessage
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
}

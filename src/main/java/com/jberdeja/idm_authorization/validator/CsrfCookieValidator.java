package com.jberdeja.idm_authorization.validator;

import java.util.Objects;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

@Component
public class CsrfCookieValidator {
    public boolean attributeCsrfTokenExists(CsrfToken csrfToken){
        return Objects.nonNull(csrfToken.getHeaderName());
    }

}

package com.jberdeja.idm_authorization.validator;

import java.util.Objects;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.utility.Utility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HeaderValidator {
    private static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

    public void validateAuthorizationExistence(String authorization){
        String errorMessage = "error, authorization is null or blank";
        Utility.validate(
            authorization, 
            Utility::isNullOrBlank, 
            errorMessage
        );
    }

    public void validateAuthorizationContent(String authorization){
        validateIfAuthorizationIsNullOrBlank(authorization);
        validateIfAuthorizationStartsWithBearer(authorization);
    }

    public void validateCsrfToken(CsrfToken csrfToken){
        String errorMessage = "error, CsrfToken is not in the request header";
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
}

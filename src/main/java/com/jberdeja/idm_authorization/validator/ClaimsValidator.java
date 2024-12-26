package com.jberdeja.idm_authorization.validator;

import java.util.Date;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.utility.Utility;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ClaimsValidator {

    public void validateClaimsExpiration(Claims claims){
        String errorMessage = "error, the claims are from an expired token";
        Utility.validate(
            claims, 
            this::isClaimsExpired, 
            errorMessage
        );
    }

    public void validateUsernamen(String username){
        String errorMessage = "error, username is null or blank";
        Utility.validate(
            username, 
            Utility::isNullOrBlank, 
            errorMessage
        );
    }

    private boolean isClaimsExpired(Claims claims){
        validateExpirationDatePresence(claims.getExpiration());
        return claims.getExpiration().before(new Date());
    }

    private void validateExpirationDatePresence(Date date){
        String errorMessage = "error, Claims does not have an expiration date";
        Utility.validate(
            date, 
            Utility::isNullObject, 
            errorMessage
        );
    }
}

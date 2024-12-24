package com.jberdeja.idm_authorization.validator;

import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.dto.validator_result.JwtValidatorResult;
import com.jberdeja.idm_authorization.utility.Utility;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtValidator {
    private static final String MSG_ERR_INVALID_TOKEN = "the token is not the authenticated user because the user associated with the token does not match the authenticated user";

    public JwtValidatorResult validateTokenBelongsToAuthenticatedUser(
        String headerAuthorization,
        Function<String, Claims> getTokenClaims, 
        Function<Claims, String> getClaimsUsername,
        Function<String, UserDetails> getUserDetailsFromDatabase) {

        Claims claims = getTokenClaims.apply(headerAuthorization);
        String username = getClaimsUsername.apply(claims);
        UserDetails userDetails = getUserDetailsFromDatabase.apply(username);
        validateIfTokenIsOfAuthenticatedUser(username, userDetails);
        return new JwtValidatorResult(userDetails);
    }
    
    private void validateIfTokenIsOfAuthenticatedUser(
        String headerAuthorizationTokenClaimsUsername, 
        UserDetails userDetailsFromDatabase) {
    
        String username = userDetailsFromDatabase.getUsername();
        
        Utility.validate(
            username, 
            headerAuthorizationTokenClaimsUsername, 
            Utility::isNotEquals, 
            MSG_ERR_INVALID_TOKEN
        );
    }
}

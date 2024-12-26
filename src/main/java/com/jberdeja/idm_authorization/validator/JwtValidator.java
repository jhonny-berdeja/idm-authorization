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

    public JwtValidatorResult validateTokenBelongsToAuthenticatedUser(
        String authorization,
        Function<String, Claims> getAuthorizationClaims, 
        Function<Claims, String> getClaimsUsername,
        Function<String, UserDetails> getUserDetailsFromDatabase) {

        Claims claims = getAuthorizationClaims.apply(authorization);
        String username = getClaimsUsername.apply(claims);
        UserDetails userDetails = getUserDetailsFromDatabase.apply(username);
        validateMatchOfTwoUsers(username, userDetails.getUsername());
        return new JwtValidatorResult(userDetails);
    }
    
    private void validateMatchOfTwoUsers(
        String claimsUsername, 
        String userDetailsUsername) {
            
        Utility.validate(
            claimsUsername, 
            userDetailsUsername, 
            Utility::isNotEquals, 
            "error, token not associated with authenticated user"
        );
    }
}

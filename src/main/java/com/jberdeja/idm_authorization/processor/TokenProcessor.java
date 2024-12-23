package com.jberdeja.idm_authorization.processor;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jberdeja.idm_authorization.connector.JwtConnector;
import com.jberdeja.idm_authorization.mapper.ClaimsMapper;
import com.jberdeja.idm_authorization.validator.JWTValidator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProcessor {
    @Autowired
    private JwtConnector jwtConnector;
    @Autowired
    private HeaderProcessor headerService;
    @Autowired
    private ClaimsMapper claimsMapper;

    public Claims getTokenClaimsFromAuthorizationHeaderValue(String headerAuthorizationValue){
        String headerAuthorizationValueToken = headerService.getAuthorizationValueTokenFromRequestHeader(headerAuthorizationValue);
        Claims headerAuthorizationValueTokenClaims = requestClaimsFromAuthorizationHeaderToken(headerAuthorizationValueToken);
        validateIfClaimsIsFromAnExpiredToken(headerAuthorizationValueTokenClaims);
        return headerAuthorizationValueTokenClaims;
    }

    public String getUsernameFromTokenClaims(Claims headerAuthorizationValueTokenClaims){
        String headerAuthorizationValueTokenClaimsUsername = headerAuthorizationValueTokenClaims.getSubject();
        validateUsernamen(headerAuthorizationValueTokenClaimsUsername);
        return headerAuthorizationValueTokenClaimsUsername;
    }

    private Claims requestClaimsFromAuthorizationHeaderToken(String headerAuthorizationValueToken){
        String claims = jwtConnector.requestTokenClaims(headerAuthorizationValueToken);
        return claimsMapper.mapToClaimsObject(claims);
    }

    public void validateIfClaimsIsFromAnExpiredToken(Claims tokenClaims){
        if(tokenClaims.getExpiration().before(new Date())){
            log.error("error, the claims are from an expired token");
            throw new IllegalArgumentException("error, the claims are from an expired token");
        }
    }

    public void validateUsernamen(String username){
        if(isUsernameInvalid(username)){
            log.error("the username is not valid because it is null or blank");
            throw new IllegalArgumentException("the username is not valid because it is null or blank");
        }
    }
    
    private boolean isUsernameInvalid(String username){
        return ! isUsernameValid(username);
    }

    private boolean isUsernameValid(String username){
        return Objects.nonNull(username) && isNotBlackUsername(username);
    }

    private boolean isNotBlackUsername(String username){
        return ! username.isBlank();
    }
}

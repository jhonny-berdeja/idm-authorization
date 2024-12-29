package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.connector.JwtConnector;
import com.jberdeja.idm_authorization.mapper.ClaimsMapper;
import com.jberdeja.idm_authorization.validator.ClaimsValidator;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClaimsProcessor {
    private static final String PREFIX_BEARER = "(?i)Bearer ";
    private static final String EMPTY = "";
    @Autowired
    private JwtConnector jwtConnector;
    @Autowired
    private ClaimsMapper claimsMapper;
    @Autowired
    private ClaimsValidator claimsValidator;

    public Claims getTokenClaims(String authorization){
        String token = getAuthorizationToken(authorization);
        Claims claims = requestTokenClaims(token);
        validateClaimsExpiration(claims);
        return claims;
    }

    public String getAuthorizationToken(String authorization){
        claimsValidator.validateAuthorizationContent(authorization);
        return authorization.replaceFirst(PREFIX_BEARER, EMPTY);
    }

    public String getClaimsUsername(Claims claims){
        String username = claims.getSubject();
        validateUsernamen(username);
        return username;
    }

    private Claims requestTokenClaims(String token){
        String claims = jwtConnector.requestTokenClaims(token);
        return claimsMapper.mapToClaimsObject(claims);
    }

    public void validateClaimsExpiration(Claims claims){
        claimsValidator.validateClaimsExpiration(claims);
    }

    public void validateUsernamen(String username){
        claimsValidator.validateUsernamen(username);
    }
}

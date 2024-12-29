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
    @Autowired
    private JwtConnector jwtConnector;
    @Autowired
    private HeaderProcessor headerProcessor;
    @Autowired
    private ClaimsMapper claimsMapper;
    @Autowired
    private ClaimsValidator claimsValidator;

    public Claims getTokenClaims(String authorization){
        String token = headerProcessor.getAuthorizationToken(authorization);
        Claims claims = requestTokenClaims(token);
        validateClaimsExpiration(claims);
        return claims;
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

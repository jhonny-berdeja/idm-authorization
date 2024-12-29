package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.dto.validator_result.JwtValidatorResult;
import com.jberdeja.idm_authorization.service.UserIdmDetailsService;
import com.jberdeja.idm_authorization.validator.JwtValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProcessor {
    @Autowired
    private JwtValidator jwtValidator;
    @Autowired
    private UserIdmDetailsService userIdmDetailsService;
    @Autowired
    private ClaimsProcessor claimsProcessor;

    public JwtValidatorResult validateTokenBelongsToAuthenticatedUser(String authHeader){
        
        JwtValidatorResult result = jwtValidator.validateTokenBelongsToAuthenticatedUser(
            authHeader, 
            claimsProcessor::getTokenClaims,
            claimsProcessor::getClaimsUsername,
            userIdmDetailsService::loadUserByUsername);

        return result;
    }
}

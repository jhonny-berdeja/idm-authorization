package com.jberdeja.idm_authorization.service;

import java.util.Date;
import java.util.Objects;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtService {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";
    private static final String PREFIX_BEARER = "(?i)Bearer ";
    private static final String EMPTY = "";

    public String obtainTokenOfHeaders(HttpServletRequest request) {
        var tokenOfHeaders = request.getHeader(AUTHORIZATION_HEADER);
        if(isNotValidTokenOfHeader(tokenOfHeaders))
            throw new IllegalArgumentException("The token header is not valid");
        return tokenOfHeaders.replaceFirst(PREFIX_BEARER, EMPTY);

    }

    public void validateUsernamen(String username){
        if(isNotValidUsername(username)){
            log.error("The token username is not valid");
            throw new IllegalArgumentException("The token username is not valid");
        }
    }

    public void validateExpirationToken(Claims claimsFromTokem){
        if(claimsFromTokem.getExpiration().before(new Date())){
            log.error("The token expired");
            throw new IllegalArgumentException("The token expired");
        }
    }

    private boolean isNotValidUsername(String username){
        return !isValidUsername(username);
    }

    private boolean isValidUsername(String username){
        return Objects.nonNull(username) && isNotBlackUsername(username);
    }

    private boolean isNotBlackUsername(String username){
        return !username.isBlank();
    }

    private boolean isNotValidTokenOfHeader(String requestTokenHeader){
        return !isValidTokenOfHeader(requestTokenHeader);
    }
    private boolean isValidTokenOfHeader(String requestTokenHeader){
        return Objects.nonNull(requestTokenHeader) 
                    && requestTokenHeader.startsWith(AUTHORIZATION_HEADER_BEARER);
    }
}

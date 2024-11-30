package com.jberdeja.idm_authorization.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jberdeja.idm_authorization.connector.JwtConnector;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
    public static final long JWT_TOKEN_VALIDATY = 5 * 60 * 60;
    public static final String JWT_SECRET = "ClientSecret1ClientSecret2ClientSecret3XXX";
    @Autowired
    private JwtConnector connector;

    public Boolean validateToken(String token, UserDetails userDetails){
        var usernameFromJwt = getUsernameFromToken(token);
        var usernameFromUserDetails = userDetails.getUsername();
        return usernameFromUserDetails.equals(usernameFromJwt) && isNotTokenExpired(token);
    }

    private Boolean isNotTokenExpired(String token){
        return !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token){
        var expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public String getUsernameFromToken(String token){
        return getClaimsFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token){
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver ){
        var claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        String responseJson = connector.getClaims(token);
        Claims claims = getClaimsFromResponse(responseJson);
        return claims;
    }

    public Claims getClaimsFromResponse(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> claimsMap = objectMapper.readValue(jsonResponse, Map.class);
            return Jwts.claims(claimsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.jberdeja.idm_authorization.service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    public static final long JWT_TOKEN_VALIDATY = 5 * 60 * 60;
    public static final String JWT_SECRET = "ClientSecret1ClientSecret2ClientSecret3XXX";

    public Boolean validateToken(String token, UserDetails userDetails){
        final var usernameFromUserDetails = userDetails.getUsername();
        final var usernameFromJWT = this.getUsernameFromToken(token);
        boolean bb = this.isNotTokenExpired(token);
        return(usernameFromUserDetails.equals(usernameFromJWT)) && bb;
    }

    private Boolean isNotTokenExpired(String token){
        return !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token){
        var expirationDate = this.getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public String getUsernameFromToken(String token){
        return this.getClaimsFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token){
        return this.getClaimsFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver ){
        var claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
    }
}

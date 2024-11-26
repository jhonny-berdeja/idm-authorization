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

    private Claims getAllClaimsFromToken(String token){
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver ){
        final var claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDateFromToken(String token){
        return this.getClaimsFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token){
        final var expirationDate = this.getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Boolean isNotTokenExpired(String token){
        return !isTokenExpired(token);
    }

    public String getUsernameFromToken(String token){
        return this.getClaimsFromToken(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails){
        final Map<String, Object> claims = Collections.singletonMap("ROLES", userDetails.getAuthorities().toString());
        return this.getToken(claims, userDetails.getUsername());
    }

    private String getToken(Map<String, Object> claims, String subject){
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDATY * 1000))
        .signWith(key)
        .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final var usernameFromUserDetails = userDetails.getUsername();
        final var usernameFromJWT = this.getUsernameFromToken(token);
        boolean bb = this.isNotTokenExpired(token);
        return(usernameFromUserDetails.equals(usernameFromJWT)) && bb;
    }
}

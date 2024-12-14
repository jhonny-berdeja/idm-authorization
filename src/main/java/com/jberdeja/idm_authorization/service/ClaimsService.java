package com.jberdeja.idm_authorization.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jberdeja.idm_authorization.connector.JwtConnector;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClaimsService {
    @Autowired
    private JwtConnector jwtConnector;
    @Autowired
    private JwtService jwtService;

    public Claims obtainCliamsOfToken(HttpServletRequest request){
        String token = jwtService.obtainTokenOfHeaders(request);
        Claims claimsOfToken = getClaimsOfToken(token);
        jwtService.validateExpirationToken(claimsOfToken);
        return claimsOfToken;
    }

    public Claims getAllClaimsFromToken(String token){
        String claimsJson = jwtConnector.obtainClaimsOfToken(token);
        Claims claimsObject = convertClaimsJsonToObjectClaims(claimsJson);
        return claimsObject;
    }

    public String obtainUsernameOfClaimasOfToken(Claims claimsOfToken){
        String usernameOfClaims = claimsOfToken.getSubject();
        jwtService.validateUsernamen(usernameOfClaims);
        return usernameOfClaims;
    }

    private Claims getClaimsOfToken(String token){
        String claimsJson = jwtConnector.obtainClaimsOfToken(token);
        Claims claims = convertJsonStringToObjectClaims(claimsJson);
        return claims;
    }

    @SuppressWarnings("unchecked")
    private Claims convertClaimsJsonToObjectClaims(String jsonResponse) {// Evalidar para llevarlo a un mapper
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> claimsMap = objectMapper.readValue(jsonResponse, Map.class);
            return Jwts.claims(claimsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Claims convertJsonStringToObjectClaims(String claimsJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> claimsMap = objectMapper.readValue(claimsJson, Map.class);
            return Jwts.claims(claimsMap);
        } catch (Exception e) {
            log.error("Error converting from json string to Claims object", e);
            throw new IllegalArgumentException("Error converting from json string to Claims object", e);
        }
    }

}

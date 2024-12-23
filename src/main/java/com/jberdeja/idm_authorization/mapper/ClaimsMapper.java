package com.jberdeja.idm_authorization.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ClaimsMapper {
    
    @SuppressWarnings("unchecked")
    public Claims mapToClaimsObject(String claims) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return Jwts.claims(objectMapper.readValue(claims, Map.class));
        } catch (Exception e) {
            log.error("error when mapping claims in string format to Claims object format", e);
            throw new IllegalArgumentException("error when mapping claims in string format to Claims object format", e);
        }
    }
}

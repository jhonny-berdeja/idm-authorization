package com.jberdeja.idm_authorization.connector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jberdeja.idm_authorization.dto.JWTAuthenticateRequest;
import com.jberdeja.idm_authorization.dto.JWTResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtConnector {
    private static final String ENDPOINT_AUTHENTICATE = "/authenticate";
    private static final String ENDPOINT_GET_ALL_CLAIMS = "/get-all-clams";
    private static final String SLASH = "/";
    @Value("${host.idm-authenticate}")
    private String hostIDMAuthenticate;
    @Value("${user.idm-authenticate}")
    private String userForAuthentication;
    @Value("${password.idm-authenticate}")
    private String passwordForAuthenticate;

    public String obtainClaimsOfToken(String pathVariable){
        try{
            JWTAuthenticateRequest body = buildBodyForAuthentication();
            JWTResponse jwtResponse = authenticateForObtainToken(body);
            return getClaimsOfToken(jwtResponse.getJwt(), pathVariable);
        }catch(Exception e){
            log.error("Error when obtaining the token Claims", e);
            throw new IllegalArgumentException("Error when obtaining the token Claims", e);
        }
    }

    private JWTAuthenticateRequest buildBodyForAuthentication(){
        return new JWTAuthenticateRequest(userForAuthentication, passwordForAuthenticate);
    }
    
    private JWTResponse authenticateForObtainToken(JWTAuthenticateRequest body){
        try{
            String url = hostIDMAuthenticate + ENDPOINT_AUTHENTICATE;
            HttpHeaders headers = buildHttpHeaders();
            ResponseEntity<JWTResponse> response = executeRequestPost(url, body, headers);
            return response.getBody();
        }catch(Exception e){
            log.error("Error requesting token to idm-authentication", e);
            throw new IllegalArgumentException("Error requesting token to idm-authentication", e);
        }
    }

    private String getClaimsOfToken(String token, String pathVariable){
        try{
            String url = hostIDMAuthenticate + ENDPOINT_GET_ALL_CLAIMS + SLASH + pathVariable;
            HttpHeaders headers = buildHttpHeaders();
            headers.setBearerAuth(token);
            ResponseEntity<String> response = executeRequestGet(url, headers);
            return response.getBody();
        }catch(Exception e){
            log.error("Error when requesting claims to idm-authentication.", e);
            throw new IllegalArgumentException("Error when requesting claims to idm-authentication.", e);
        }
    }

    private HttpHeaders buildHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private ResponseEntity<String> executeRequestGet(String url, HttpHeaders headers){
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    private ResponseEntity<JWTResponse> executeRequestPost(String url, JWTAuthenticateRequest body, HttpHeaders headers){
        HttpEntity<JWTAuthenticateRequest> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JWTResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                JWTResponse.class
        );
        return response;
    }
}

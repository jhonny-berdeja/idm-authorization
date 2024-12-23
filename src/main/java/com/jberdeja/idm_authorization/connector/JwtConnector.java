package com.jberdeja.idm_authorization.connector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.jberdeja.idm_authorization.dto.JwtAuthenticationRequest;
import com.jberdeja.idm_authorization.dto.JwtAuthenticationResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtConnector {
    private static final String ENDPOINT_AUTHENTICATE = "/authenticate";
    private static final String ENDPOINT_GET_ALL_CLAIMS = "/get-all-clams";
    private static final String SLASH = "/";
    @Value("${host.idm-authenticate}")
    private String hostIdmAuthentication;
    @Value("${user.idm-authenticate}")
    private String userIdmAuthenticate;
    @Value("${password.idm-authenticate}")
    private String passwordIdmAuthenticate;

    public String requestTokenClaims(String tokenForPathVariable){
        JwtAuthenticationRequest body = buildJwtAuthenticationRequestBody();
        JwtAuthenticationResponse jwtAuthenticationResponse = requestAuthenticationToken(body);
        String tokenAuthenticated = jwtAuthenticationResponse.getJwt();
        return requestTokenClaims(tokenAuthenticated, tokenForPathVariable);
    }

    private JwtAuthenticationRequest buildJwtAuthenticationRequestBody(){
        return new JwtAuthenticationRequest(userIdmAuthenticate, passwordIdmAuthenticate);
    }
    
    private JwtAuthenticationResponse requestAuthenticationToken(JwtAuthenticationRequest body){
        try{
            String url = hostIdmAuthentication + ENDPOINT_AUTHENTICATE;
            HttpHeaders headers = buildHttpHeaders();
            ResponseEntity<JwtAuthenticationResponse> response = requestAuthenticationToken(url, body, headers);
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error requesting token to idm-authentication", e);
            throw new IllegalArgumentException("Error requesting token to idm-authentication", e);
        } catch (Exception e) {
            log.error("Unexpected error requesting token to idm-authentication", e);
            throw new IllegalArgumentException("Unexpected error requesting token to idm-authentication", e);
        }
    }

    private String requestTokenClaims(String tokenAuthenticated, String tokenForPathVariable){
        try{
            String url = hostIdmAuthentication + ENDPOINT_GET_ALL_CLAIMS + SLASH + tokenForPathVariable;
            HttpHeaders headers = buildHttpHeaders();
            headers.setBearerAuth(tokenAuthenticated);
            ResponseEntity<String> response = requestTokenClaims(url, headers);
            return response.getBody();
        }catch (RestClientException e) {
            log.error("Error requesting claims to idm-authentication", e);
            throw new IllegalArgumentException("Error requesting token to idm-authentication", e);
        } catch (Exception e) {
            log.error("Unexpected error requesting claims to idm-authentication", e);
            throw new IllegalArgumentException("Unexpected error requesting claims to idm-authentication", e);
        }
    }

    private HttpHeaders buildHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private ResponseEntity<String> requestTokenClaims(String url, HttpHeaders headers){
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    private ResponseEntity<JwtAuthenticationResponse> requestAuthenticationToken(String url, JwtAuthenticationRequest body, HttpHeaders headers){
        HttpEntity<JwtAuthenticationRequest> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                JwtAuthenticationResponse.class
        );
        
    }
}

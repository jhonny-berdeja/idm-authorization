package com.jberdeja.idm_authorization.connector;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jberdeja.idm_authorization.entityes.JWTAuthenticateRequest;
import com.jberdeja.idm_authorization.entityes.JWTResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtConnector {

        public String getClaims(String jwt){
            try{
                return getAllClaimsFromToken(jwt);
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }
        private String getToken(){
            try{
                return login().getJwt();
            }catch(Exception e){
                throw new RuntimeException(e);
            }
            
        }

        private JWTResponse login(){
        
            String url = "http://localhost:8080/authenticate";

            JWTAuthenticateRequest body = new JWTAuthenticateRequest("admin", "to_be_encoded");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<JWTAuthenticateRequest> requestEntity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JWTResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    JWTResponse.class
            );

            return response.getBody();
        }

        private String getAllClaimsFromToken(String request){
        
            String url = "http://localhost:8080/get-all-clams/" + request;

            String token =  getToken();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            return response.getBody();
        }

}

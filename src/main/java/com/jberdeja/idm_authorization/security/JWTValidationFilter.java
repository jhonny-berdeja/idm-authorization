package com.jberdeja.idm_authorization.security;

import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jberdeja.idm_authorization.service.ClaimsService;
import com.jberdeja.idm_authorization.service.UserIDMDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter{

    @Autowired
    private UserIDMDetailsService userIDMDetailsService;
    @Autowired
    private ClaimsService claimsService;

    @Override
    protected void doFilterInternal(
                                    HttpServletRequest request
                                    , HttpServletResponse response
                                    , FilterChain filterChain) throws ServletException, IOException {
        try {
            //falta excluir el filtro para la ruta /hello , copiar de idm-authentication
            validateAuthenticationInContextOfCurrentTread();
            Claims claimsOfToken = claimsService.obtainCliamsOfToken(request);
            String usernameFromToken = claimsService.obtainUsernameOfClaimasOfToken(claimsOfToken);
            UserDetails userDetailsFromDatabase = obtainUserDetailsOfDatabase(usernameFromToken);
            validateIfTokenIsOfAuthenticatedUser(usernameFromToken, userDetailsFromDatabase);
            addAuthenticationToContextOfCurrentTread(userDetailsFromDatabase, request);
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            log.error("Eror validate token", e);
            throw new RuntimeException("Eror validate token", e);
        }
    }

    private UserDetails obtainUserDetailsOfDatabase(String username){
        return userIDMDetailsService.loadUserByUsername(username);
    }

    private void addAuthenticationToContextOfCurrentTread(UserDetails userDetailsFromDatabase, HttpServletRequest request){
        var usernameAndPasswordAuthToken = buildUsernamePasswordAuthenticationToken(userDetailsFromDatabase, request);
        addAuthenticationToContextOfCurrentTread(usernameAndPasswordAuthToken);
    }

    private void addAuthenticationToContextOfCurrentTread(UsernamePasswordAuthenticationToken usernameAndPasswordAuthToken){
        SecurityContextHolder.getContext().setAuthentication(usernameAndPasswordAuthToken);
    }

    private UsernamePasswordAuthenticationToken buildUsernamePasswordAuthenticationToken(UserDetails userDetails, HttpServletRequest request){
        var usernameAndPasswordAuthToken = new UsernamePasswordAuthenticationToken(
            userDetails
            , null
            , userDetails.getAuthorities());
        usernameAndPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernameAndPasswordAuthToken;
    }

    private void validateAuthenticationInContextOfCurrentTread(){
        if(existAuthenticationInContextOfCurrentTread()){
            log.error("An authentication already exists in the context of the current thread");
            throw new RuntimeException("An authentication already exists in the context of the current thread");
        }
    }

    private boolean existAuthenticationInContextOfCurrentTread(){
        if(Objects.isNull(SecurityContextHolder.getContext().getAuthentication()))
            return false;
        return true;
    }
    
    private void validateIfTokenIsOfAuthenticatedUser(String usernameFromToken, UserDetails userDetailsFromDatabase ){
        String usernameFromDatabase = userDetailsFromDatabase.getUsername();
        if(isNotTokenOwnedByAuthenticatedUser(usernameFromToken, usernameFromDatabase)){
            log.error("Error the token is not from the authenticated user");
            throw new IllegalArgumentException("Error the token is not from the authenticated user");
        }
    }

    private boolean isNotTokenOwnedByAuthenticatedUser(String usernameFromToken, String usernameFromDatabase){
        return !isTokenOwnedByAuthenticatedUser(usernameFromToken, usernameFromDatabase);
    }

    private boolean isTokenOwnedByAuthenticatedUser(String usernameFromToken, String usernameFromDatabase){
        return usernameFromToken.equalsIgnoreCase(usernameFromDatabase);
    }
}
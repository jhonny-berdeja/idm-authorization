package com.jberdeja.idm_authorization.security;

import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jberdeja.idm_authorization.service.JwtService;
import com.jberdeja.idm_authorization.service.UserIDMDetailsService;

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
    private final JwtService jwtService;
    private final UserIDMDetailsService jwtUserDetailService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(
                                    HttpServletRequest request
                                    , HttpServletResponse response
                                    , FilterChain filterChain) throws ServletException, IOException {
        try {
            var jwt = obtainJwt(request);
            var username = jwtService.getUsernameFromToken(jwt);
            var userDetails = obtainUserDetails(username);
            validateToken(jwt, userDetails);
            var usernameAndPasswordAuthToken = buildUsernamePasswordAuthenticationToken(userDetails);
            usernameAndPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernameAndPasswordAuthToken);
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            log.error("Eror validate token" + e);
        }
    }

    private void validateToken(String jwt, UserDetails userDetails){
        if(isNotValidToken(jwt, userDetails))
        throw new RuntimeException("The token is not valid");
    }

    private boolean isNotValidToken(String jwt, UserDetails userDetails){
        return !isValidToken(jwt, userDetails);
    }
    private boolean isValidToken(String jwt, UserDetails userDetails){
       return jwtService.validateToken(jwt, userDetails);
    }
    private UserDetails obtainUserDetails(String username){
        if(isNotValidUsername(username))
            throw new RuntimeException("The username is not valid");
        return this.jwtUserDetailService.loadUserByUsername(username);
    }
    private boolean isNotValidUsername(String username){
        return !isValidUsername(username);
    }
    private boolean isValidUsername(String username){
        return Objects.nonNull(username) 
                    && Objects.isNull(
                            SecurityContextHolder.getContext().getAuthentication()
                        );
    }

    private UsernamePasswordAuthenticationToken buildUsernamePasswordAuthenticationToken(UserDetails userDetails){
        return new UsernamePasswordAuthenticationToken(
            userDetails
            , null
            , userDetails.getAuthorities());
    }

    private String obtainJwt(HttpServletRequest request){
        var requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);
        if(isNotValidTokenHeader(requestTokenHeader))
            throw new RuntimeException("The token header is not valid");
        return requestTokenHeader.substring(7);
    }

    private boolean isNotValidTokenHeader(String requestTokenHeader){
        return !isValidTokenHeader(requestTokenHeader);
    }
    private boolean isValidTokenHeader(String requestTokenHeader){
        return Objects.nonNull(requestTokenHeader) 
                    && requestTokenHeader.startsWith(AUTHORIZATION_HEADER_BEARER);
    }
}
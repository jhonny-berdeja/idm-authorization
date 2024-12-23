package com.jberdeja.idm_authorization.security.filter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jberdeja.idm_authorization.dto.JwtValidatorResult;
import com.jberdeja.idm_authorization.processor.HeaderProcessor;
import com.jberdeja.idm_authorization.processor.HttpServletRequestProcessor;
import com.jberdeja.idm_authorization.processor.SecurityContextHolderProcessor;
import com.jberdeja.idm_authorization.validator.JWTValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter{
    private static final String URL_HELLO = "http://localhost/hello";

    @Autowired
    private JWTValidator jwtValidator;
    @Autowired
    private HttpServletRequestProcessor httpServletRequestService;
    @Autowired
    private HeaderProcessor headerService;
    @Autowired
    private SecurityContextHolderProcessor securityContextHolderService;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(
                                    HttpServletRequest request
                                    , HttpServletResponse response
                                    , FilterChain filterChain) throws ServletException, IOException {
        String requestURL = httpServletRequestService.obtainRequestURL(request);
        if(itIsNecessaryToValidate(requestURL)) applyValidations(request);
        filterChain.doFilter(request, response);
    }

    private void applyValidations(HttpServletRequest request){
        String headerAuthorizationValue = httpServletRequestService.getHeaderAuthorizationValue(request);
        headerService.validateHeaderAuthorizationValue(headerAuthorizationValue);
        JwtValidatorResult jwtValidatorResult = jwtValidator.validateIfTokenIsOfAuthenticatedUser(headerAuthorizationValue);
        UserDetails userDetailsOfDatabase = jwtValidatorResult.getUserDetailsOfDatabase();
        securityContextHolderService.validateAuthenticationInCurrentThreadContext();
        securityContextHolderService.addAuthenticationToTheCurrentThreadContext(userDetailsOfDatabase, request);
    }

    private boolean itIsNecessaryToValidate(String requestURL){
        return ! itNotNecessaryToValidate(requestURL);
    }

    private boolean itNotNecessaryToValidate(String requestURL){
        return isLocalOrDevelopmentEnvironment() && isURLFree(requestURL);
    }

    private boolean isLocalOrDevelopmentEnvironment(){
        // TODO Implementar este metodo
        return Boolean.TRUE;
    }

    private boolean isURLFree(String requestURL){
        return URL_HELLO.equalsIgnoreCase(requestURL);
    }
}
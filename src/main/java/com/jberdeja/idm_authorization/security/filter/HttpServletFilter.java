package com.jberdeja.idm_authorization.security.filter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jberdeja.idm_authorization.processor.HttpServletFilterProcessor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpServletFilter extends OncePerRequestFilter{

    @Autowired 
    private HttpServletFilterProcessor httpServletFilterProcessor;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    )throws ServletException, IOException {

        var authorization = applyAuthorizationValidation(request, response);
        var xXsrfToken = applyXxsrfTokenValidation(request, response);
        var cookie = applyCookieValidation(request, response);
        var csrfToken = applyCsrfTokenValidation(request, response);

        var isNecessaryToFilter = isNecessaryToFilter(
            authorization, 
            xXsrfToken, 
            cookie,
            csrfToken
        );

        if(isNecessaryToFilter)
            filterChain.doFilter(request, response);   
             
    }

    private boolean isNecessaryToFilter(
        boolean authorization, 
        boolean xXsrfToken, 
        boolean cookie,
        boolean csrfToken
    ){

        return httpServletFilterProcessor.isNecessaryToFilter(
            authorization, 
            xXsrfToken, 
            cookie,
            csrfToken
        );
    }

    private boolean applyAuthorizationValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return httpServletFilterProcessor.applyAuthorizationValidation(
            request, 
            response
        );
    }

    private boolean applyXxsrfTokenValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return httpServletFilterProcessor.applyXxsrfTokenValidation(
            request, 
            response
        );
    }

    private boolean applyCookieValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return httpServletFilterProcessor.applyCookieValidation(
            request, 
            response
        );
    }

    private boolean applyCsrfTokenValidation(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws IOException{

        return httpServletFilterProcessor.applyCsrfTokenValidation(
            request, 
            response
        );
    }

    
}

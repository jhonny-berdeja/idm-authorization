package com.jberdeja.idm_authorization.security.filter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jberdeja.idm_authorization.processor.HeaderProcessor;
import com.jberdeja.idm_authorization.processor.HttpServletRequestProcessor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CsrfCookieFilter extends OncePerRequestFilter{
    @Autowired
    private HttpServletRequestProcessor httpServletRequestProcessor;
    @Autowired
    private HeaderProcessor headerProcessor;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) 
                                    throws ServletException, IOException{                 
        CsrfToken csrfToken = getHeaderCsrfToken(request);                    
        validateCsrfToken(csrfToken);
        addCsrfTokenToResponseHeaderForNextRequest(response, csrfToken);
        filterChain.doFilter(request, response);                
    }

    private CsrfToken getHeaderCsrfToken(HttpServletRequest request){
        return httpServletRequestProcessor.getHeaderCsrfToken(request);   
    }

    private void validateCsrfToken(CsrfToken csrfToken ){
        headerProcessor.validateCsrfToken(csrfToken);
    }

    private void addCsrfTokenToResponseHeaderForNextRequest(HttpServletResponse response, CsrfToken csrfToken){
        httpServletRequestProcessor.addCsrfTokenToResponseHeaderForNextRequest(response, csrfToken);
    }

}

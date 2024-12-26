package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.validator.SecurityContextHolderValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecurityContextHolderProcessor {
    private static final Object OBJECT_NULL = null;

    @Autowired
    private SecurityContextHolderValidator securityContextHolderValidator;

    public void verifyIfAuthenticationIsPresentInContext(){
        securityContextHolderValidator.verifyIfAuthenticationIsPresentInContext();
    }

    public void setAuthenticationInContext(
        UserDetails userDetails, 
        HttpServletRequest request
    ){
        var usernamePasswordAuthenticationToken = buildUsernamePasswordAuthenticationToken(
            userDetails, 
            request
        );
        setAuthenticationInContext(usernamePasswordAuthenticationToken);
    }

    private void setAuthenticationInContext(
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
    ){   

        securityContextHolderValidator.validateContext();
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private UsernamePasswordAuthenticationToken buildUsernamePasswordAuthenticationToken(
        UserDetails userDetails, 
        HttpServletRequest request
    ){

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            OBJECT_NULL,
            userDetails.getAuthorities()
        );
        WebAuthenticationDetails webAuthenticationDetails = buildWebAuthenticationDetails(request);
        usernamePasswordAuthenticationToken.setDetails(webAuthenticationDetails);
        return usernamePasswordAuthenticationToken;
    }

    private WebAuthenticationDetails buildWebAuthenticationDetails(HttpServletRequest request){
        return new WebAuthenticationDetailsSource().buildDetails(request);
    }
}

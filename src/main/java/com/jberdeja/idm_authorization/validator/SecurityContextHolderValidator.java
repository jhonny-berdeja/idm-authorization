package com.jberdeja.idm_authorization.validator;

import java.util.Objects;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SecurityContextHolderValidator {
    public void validateAuthenticationInCurrentThreadContext(){
        if(exitsAuthenticationInCurrentThreadContext()){
            log.error("error, authentication already exists in the context of the current thread");
            throw new IllegalStateException("error, authentication already exists in the context of the current thread");
        }
    }

    private boolean exitsAuthenticationInCurrentThreadContext(){
        return ! doesNotExitsAuthenticationInCurrentThreadContext();
    }

    private boolean doesNotExitsAuthenticationInCurrentThreadContext(){
        return Objects.isNull(SecurityContextHolder.getContext().getAuthentication());
    }
}

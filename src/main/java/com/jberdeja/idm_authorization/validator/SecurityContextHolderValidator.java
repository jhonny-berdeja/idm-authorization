package com.jberdeja.idm_authorization.validator;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.utility.Utility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SecurityContextHolderValidator {
    public void verifyIfAuthenticationIsPresentInContext(){
        Utility.validate(
            this::isAuthenticationPresentInContext, 
            "error, an authentication already exists in the context of the current thread"
        );
    }

    public void validateContext(){
        Utility.validate(
            this::isNullContext,
             "error, current thread context is null"
        );
    }

    private boolean isAuthenticationPresentInContext(){
        return isNotNullContext() && isNotNullAuthentication();
    }
    
    private boolean isNullContext(){
        return Utility.isNullObject(SecurityContextHolder.getContext());
    }

    private boolean isNotNullContext(){
        return Utility.isNullObject(SecurityContextHolder.getContext());
    }

    private boolean isNotNullAuthentication(){
        return Utility.isNullObject(
            SecurityContextHolder.getContext().getAuthentication()
        );
    }
}

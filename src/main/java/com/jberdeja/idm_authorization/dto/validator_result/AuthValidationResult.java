package com.jberdeja.idm_authorization.dto.validator_result;

import lombok.Data;

@Data
public class AuthValidationResult {
    private boolean isNecessaryToFilter;
   
    public AuthValidationResult(boolean isNecessaryToFilter){
        this.isNecessaryToFilter = isNecessaryToFilter;
    }
}

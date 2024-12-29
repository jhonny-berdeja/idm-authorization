package com.jberdeja.idm_authorization.dto.validator_result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class XxssrfTokenValidationResult {
    private boolean isNecessaryToFilter;
}

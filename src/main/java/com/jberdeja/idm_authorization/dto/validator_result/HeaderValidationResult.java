package com.jberdeja.idm_authorization.dto.validator_result;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HeaderValidationResult {
    private boolean isNecessaryToFilter;
}

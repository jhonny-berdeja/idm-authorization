package com.jberdeja.idm_authorization.dto.validator_result;

import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtValidatorResult {
    private UserDetails userDetailsOfDatabase;
}

package com.jberdeja.idm_authorization.dto;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtValidatorResult {
    private UserDetails userDetailsOfDatabase;
}

package com.jberdeja.idm_authorization.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthenticationRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}

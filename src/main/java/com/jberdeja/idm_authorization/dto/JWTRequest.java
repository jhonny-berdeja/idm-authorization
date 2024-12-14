package com.jberdeja.idm_authorization.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTRequest {
    @NotBlank
    private String jwt;
}

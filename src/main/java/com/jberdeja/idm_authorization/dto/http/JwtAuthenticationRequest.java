package com.jberdeja.idm_authorization.dto.http;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthenticationRequest {
    private String username;
    private String password;
}

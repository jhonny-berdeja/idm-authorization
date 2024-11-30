package com.jberdeja.idm_authorization.entityes;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTAuthenticateRequest {
    private String username;
    private String password;
}

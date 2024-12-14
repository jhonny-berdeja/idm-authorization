package com.jberdeja.idm_authorization.dto;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserIDMRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private List<String> roles;
}

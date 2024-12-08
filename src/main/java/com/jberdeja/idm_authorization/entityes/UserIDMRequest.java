package com.jberdeja.idm_authorization.entityes;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserIDMRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private List<String> roles;
}

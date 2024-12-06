package com.jberdeja.idm_authorization.entityes;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserIDMRequest {
    private String email;
    private String password;
    private List<String> roles;
}

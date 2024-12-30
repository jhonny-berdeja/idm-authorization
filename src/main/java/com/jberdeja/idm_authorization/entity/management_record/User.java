package com.jberdeja.idm_authorization.entity.management_record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class User {
    private String names;
    private String lastnames;
    private String email;
    private String userPrincipalName;
}

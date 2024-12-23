package com.jberdeja.idm_authorization.entity.management_record;

import lombok.Data;

@Data
public class User {
    private String names;
    private String lastnames;
    private String email;
    private String userPrincipalName;
}

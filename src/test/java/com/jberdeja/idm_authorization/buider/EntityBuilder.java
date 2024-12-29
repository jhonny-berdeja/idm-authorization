package com.jberdeja.idm_authorization.buider;

import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.entity.RoleIdmEntity;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;

public class EntityBuilder {
    public static ApplicationEntity buildApplicationEntity(){
        return ApplicationEntity.builder()
        .name("Application name")
        .description("Test Description")
        .build();
    }

    public static UserIdmEntity buildUserIdmEntity(){
        RoleIdmEntity roleIdmEntity = buildRoleIdmEntity();
        return UserIdmEntity.builder()
        .email("user@example.com")
        .pwd("securePassword")
        .addRole(roleIdmEntity)
        .build();
    }

    public static RoleIdmEntity buildRoleIdmEntity(){
        return  RoleIdmEntity.builder()
        .roleName("ROLE_ADMIN")
        .description("Administrator role with all permissions")
        .build();
    }
}

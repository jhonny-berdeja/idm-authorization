package com.jberdeja.idm_authorization;

import com.jberdeja.idm_authorization.entity.ApplicationEntity;

public class EntityBuilder {
    public static ApplicationEntity buildApplicationEntity(){
        return new ApplicationEntity.Builder()
        .name("Application name")
        .description("Test Description")
        .build();
    }
}

package com.jberdeja.idm_authorization.entity.management_record;

import java.util.List;
import lombok.Data;

@Data
public class Application {
    private String name;
    private List<Role> roles;
}
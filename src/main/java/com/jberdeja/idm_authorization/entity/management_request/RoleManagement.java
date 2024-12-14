package com.jberdeja.idm_authorization.entity.management_request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleManagement {
    @NotNull
    private String name;
    @NotNull
    private String value;
    private List<ContextManagement> contexts;
}

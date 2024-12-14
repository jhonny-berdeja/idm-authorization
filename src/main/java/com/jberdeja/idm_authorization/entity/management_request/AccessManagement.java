package com.jberdeja.idm_authorization.entity.management_request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessManagement {
    @NotNull
    private String accessManagementRequesterEmail;
    @NotNull
    private String accessManagementForUserEmail;
    @NotNull
    private List<RoleManagement> roles;
}

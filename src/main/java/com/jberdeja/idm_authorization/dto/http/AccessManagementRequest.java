package com.jberdeja.idm_authorization.dto.http;

import com.jberdeja.idm_authorization.entity.management_request.AccessManagement;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessManagementRequest {
    @NotNull
    private AccessManagement accessManagement;
}

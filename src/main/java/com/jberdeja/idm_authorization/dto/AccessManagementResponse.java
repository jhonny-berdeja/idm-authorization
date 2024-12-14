package com.jberdeja.idm_authorization.dto;

import com.jberdeja.idm_authorization.entity.AccessManagementDocumentationEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessManagementResponse {
    private AccessManagementDocumentationEntity accessManagementDocumentationEntity;
}

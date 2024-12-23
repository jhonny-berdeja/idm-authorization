package com.jberdeja.idm_authorization.dto.http;

import com.jberdeja.idm_authorization.entity.ManagementRecordEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessManagementResponse {
    private ManagementRecordEntity managementRecordEntity;
}

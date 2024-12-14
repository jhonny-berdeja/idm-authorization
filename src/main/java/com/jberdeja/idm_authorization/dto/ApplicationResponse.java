package com.jberdeja.idm_authorization.dto;

import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationResponse {
    private ApplicationEntity application;
}

package com.jberdeja.idm_authorization.dto.http;

import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationResponse {
    private ApplicationEntity applicationEntity;
}

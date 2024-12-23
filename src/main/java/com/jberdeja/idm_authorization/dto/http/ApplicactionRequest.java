package com.jberdeja.idm_authorization.dto.http;

import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicactionRequest {
    @NotNull
    private ApplicationEntity applicationEntity;
}

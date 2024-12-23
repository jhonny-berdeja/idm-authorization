package com.jberdeja.idm_authorization.dto.http;

import com.jberdeja.idm_authorization.dto.common.UserIdm;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserIdmRequest {
    @NotNull
    private UserIdm userIdm;
}

package com.jberdeja.idm_authorization.processor;
import com.jberdeja.idm_authorization.dto.validator_result.HeaderValidationResult;
import jakarta.servlet.http.HttpServletRequest;

public interface Processor {
    public HeaderValidationResult applyValidations(HttpServletRequest request);
}

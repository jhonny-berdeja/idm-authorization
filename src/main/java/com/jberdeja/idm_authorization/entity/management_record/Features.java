package com.jberdeja.idm_authorization.entity.management_record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Features {
    private String name;
    private String value;
}

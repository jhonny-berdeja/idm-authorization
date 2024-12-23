package com.jberdeja.idm_authorization.entity.management_record;

import java.util.List;
import lombok.Data;

@Data
public class Role {
    private Features features;
    private List<Features> contexts;
}

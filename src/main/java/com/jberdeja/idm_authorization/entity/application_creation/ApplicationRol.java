package com.jberdeja.idm_authorization.entity.application_creation;

import java.util.List;
import lombok.Data;

@Data
public class ApplicationRol {
    private AccessData accessData;
    private List<AccessData> contexts;
}

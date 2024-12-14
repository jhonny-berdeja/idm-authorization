package com.jberdeja.idm_authorization.entity.creation_application;

import java.util.List;
import lombok.Data;

@Data
public class ApplicationRol {
    private AccessData accessData;
    private List<AccessData> contexts;
}

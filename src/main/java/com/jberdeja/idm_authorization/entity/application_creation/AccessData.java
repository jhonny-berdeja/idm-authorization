package com.jberdeja.idm_authorization.entity.application_creation;

import java.util.List;

import lombok.Data;

@Data
public class AccessData {
    private String name;
    private List<String> possibleValues;
    private String description;
}

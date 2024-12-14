package com.jberdeja.idm_authorization.entity.creation_application;

import java.util.List;

import lombok.Data;

@Data
public class AccessData {
    private String name;
    private List<String> possibleValues;
    private String description;
}

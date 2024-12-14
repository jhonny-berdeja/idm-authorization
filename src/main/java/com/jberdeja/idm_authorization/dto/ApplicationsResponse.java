package com.jberdeja.idm_authorization.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationsResponse {
    private List<String> applications;
}

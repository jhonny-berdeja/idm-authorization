package com.jberdeja.idm_authorization.dto.http;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationsResponse {
    private List<String> applicationNames;
}

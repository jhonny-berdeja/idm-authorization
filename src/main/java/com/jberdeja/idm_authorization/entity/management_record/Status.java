package com.jberdeja.idm_authorization.entity.management_record;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Status {
    private Integer number;
    private User transitioner;
    private String currentStatus;
    private String originStatus;
    private Date dateTransition;
    private List<String> followingPossibleStates;
}

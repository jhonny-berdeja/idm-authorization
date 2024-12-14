package com.jberdeja.idm_authorization.entity.management_documentation;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Status {
    private Integer number;
    private User transitioner;
    private String currentStatus;
    private String originStatus;
    private Date dateTransition;
    private List<String> followingPossibleStates;
}

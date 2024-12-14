package com.jberdeja.idm_authorization.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.jberdeja.idm_authorization.entity.management_documentation.Application;
import com.jberdeja.idm_authorization.entity.management_documentation.Status;
import com.jberdeja.idm_authorization.entity.management_documentation.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "access_management_documentation")
public class AccessManagementDocumentationEntity {
    private Integer documentationNumber;
    private String documentationId;
    private String requestType;
    private User accessManagementCreator;
    private User accessManagementFor;
    private List<Status> transitions;
    private Date lastUpdateDate;
    private Date documentCreationDate;
    private Application application;
}

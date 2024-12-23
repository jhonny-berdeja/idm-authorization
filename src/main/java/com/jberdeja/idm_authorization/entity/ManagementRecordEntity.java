package com.jberdeja.idm_authorization.entity;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.jberdeja.idm_authorization.entity.management_record.Application;
import com.jberdeja.idm_authorization.entity.management_record.Status;
import com.jberdeja.idm_authorization.entity.management_record.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "access_management_documentation")
public class ManagementRecordEntity {
    @Id
    private ObjectId id;
    private Integer identifierNumber;
    private String identifier;
    private String requestType;
    private User managementRecordCreator;
    private User accessManagementFor;
    private List<Status> transitions;
    private Date lastUpdateDate;
    private Date managementRecordCreationDate;
    private Application application;
}

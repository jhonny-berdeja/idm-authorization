package com.jberdeja.idm_authorization.buider;

import java.util.Date;
import java.util.List;

import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.entity.ManagementRecordEntity;
import com.jberdeja.idm_authorization.entity.RoleIdmEntity;
import com.jberdeja.idm_authorization.entity.RosterEntity;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;
import com.jberdeja.idm_authorization.entity.management_record.Application;
import com.jberdeja.idm_authorization.entity.management_record.Features;
import com.jberdeja.idm_authorization.entity.management_record.Role;
import com.jberdeja.idm_authorization.entity.management_record.Status;
import com.jberdeja.idm_authorization.entity.management_record.User;

public class EntityBuilder {
    public static ApplicationEntity buildApplicationEntity(){
        return ApplicationEntity.builder()
        .name("Application name")
        .description("Test Description")
        .build();
    }

    public static UserIdmEntity buildUserIdmEntity(){
        RoleIdmEntity roleIdmEntity = buildRoleIdmEntity();
        return UserIdmEntity.builder()
        .email("user@example.com")
        .pwd("securePassword")
        .roles(List.of(roleIdmEntity))
        .build();
    }

    public static RoleIdmEntity buildRoleIdmEntity(){
        return  RoleIdmEntity.builder()
        .roleName("ROLE_ADMIN")
        .description("Administrator role with all permissions")
        .build();
    }

    public static ManagementRecordEntity buildManagementRecordEntity(){
        return ManagementRecordEntity.builder()
        .identifierNumber(1)
        .identifier("IDM-1")
        .requestType("Alta")
        .managementRecordCreator(buildUSer())
        .accessManagementFor(buildUSer())
        .transitions(List.of(buildStatus()))
        .lastUpdateDate(new Date())
        .managementRecordCreationDate(new Date())
        .application(null)
        .build();
    }

    public static Application buildApplication(){
        return Application.builder()
        .name("ADMIN")
        .roles(List.of(buildRole()))
        .build();
    }

    public static Role buildRole(){
        return Role.builder()
        .features(buildFeatures())
        .contexts(List.of(buildFeatures()))
        .build();
    }

    public static Features buildFeatures(){
        return Features.builder()
        .name("ADMIN")
        .value("ADMIN")
        .build();
    }

    public static Status buildStatus(){
        return Status.builder()
        .number(1)
        .transitioner(buildUSer())
        .currentStatus("OPEN")
        .originStatus("OPEN")
        .dateTransition(new Date())
        .followingPossibleStates(List.of("APROVED", "CLOSED", "REFUSED"))
        .build();
    }

    public static User buildUSer(){
        return User.builder()
        .email("email.test@example.com")
        .names("Pedro")
        .lastnames("Test")
        .userPrincipalName("pedro.test@ar.infra.d")
        .build();
    }
    public static RosterEntity buildRosterEntity(){
        return RosterEntity.builder()
        .name("Pedro")
        .lastname("Test")
        .email("pedro.test@example.com")
        .userPrincipalName("pedro.test@ar.infra.d")
        .build();
    }
}

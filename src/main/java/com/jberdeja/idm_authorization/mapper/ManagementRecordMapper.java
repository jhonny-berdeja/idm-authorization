package com.jberdeja.idm_authorization.mapper;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.entity.ManagementRecordEntity;
import com.jberdeja.idm_authorization.entity.RosterEntity;
import com.jberdeja.idm_authorization.entity.management_record.Status;
import com.jberdeja.idm_authorization.entity.management_record.User;
import com.jberdeja.idm_authorization.entity.management_request.AccessManagement;
import com.jberdeja.idm_authorization.executor.ManagementRecordRepositoryExecutor;
import com.jberdeja.idm_authorization.executor.RosterRepositoryExecutor;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ManagementRecordMapper {
    private static final String IDM = "IDM-";
    private static final Integer UNO = 1;
    private static final Integer FIRST_STATUS= 1;
    private static final String CREATED = "created";
    private static final List<String> FOLLOWING_POSSIBLES_STATES = List.of("APPROVED", "REJECTED", "CLOSED");

    @Autowired
    private ManagementRecordRepositoryExecutor managementRecordRepositoryExecutor;
    @Autowired
    private RosterRepositoryExecutor rosterRepositoryExecutor;

    public ManagementRecordEntity mapToManagementRecordEntity(AccessManagement accessManagement){
        var managementRecordEntity = new ManagementRecordEntity();

        Integer newIdentifierNumber = buildNewIdentifierNumber();
        managementRecordEntity.setIdentifierNumber(newIdentifierNumber);

        String newIdentifier = buildNewIdentifier(newIdentifierNumber);
        managementRecordEntity.setIdentifier(newIdentifier);

        User managementRecordCreatorUser = buildManagementRecordCreator(accessManagement.getAccessManagementRequesterEmail());
        managementRecordEntity.setManagementRecordCreator(managementRecordCreatorUser);

        User accessManagementForUser = buildAccessManagementFor(accessManagement.getAccessManagementForUserEmail());
        managementRecordEntity.setAccessManagementFor(accessManagementForUser);

        List<Status> transitions = buildTransitions(accessManagement.getAccessManagementRequesterEmail());
        managementRecordEntity.setTransitions(transitions);

        Date nowDate = new Date();

        managementRecordEntity.setLastUpdateDate(nowDate);
        managementRecordEntity.setManagementRecordCreationDate(nowDate);

        return managementRecordEntity;
    }

    private String buildNewIdentifier(Integer newIdentifierNumber){
        return IDM + newIdentifierNumber;
    }

    private Integer buildNewIdentifierNumber(){
        //tener en cuenta para aplicar concurrencia para poder obtener realmete el nuevo numero
        var managementRecordEntityWithLastCreationDate = findFirstByOrderByManagementRecordEntityCreationDateDesc();
        if (doesNotExistsAnyManagementRecord(managementRecordEntityWithLastCreationDate)) return UNO;
        return managementRecordEntityWithLastCreationDate.getIdentifierNumber() + UNO;
    }

    private User buildManagementRecordCreator(String accessManagementRequesterEmail){
        RosterEntity roster = getRosterEntityByEmail(accessManagementRequesterEmail);
        return User.builder()
        .email(accessManagementRequesterEmail)
        .names(roster.getName())
        .lastnames(roster.getLastname())
        .userPrincipalName(roster.getUserPrincipalName())
        .build();
    }

    private User buildAccessManagementFor(String accessManagementForUserEmail){
        RosterEntity roster = getRosterEntityByEmail(accessManagementForUserEmail);
        return User.builder()
        .email(accessManagementForUserEmail)
        .names(roster.getName())
        .lastnames(roster.getLastname())
        .userPrincipalName(roster.getUserPrincipalName())
        .build();
    } 
    private List<Status> buildTransitions(String accessManagementRequesterEmail){
        Status status = Status.builder()
        .number(FIRST_STATUS)
        .transitioner(completTransitioner(accessManagementRequesterEmail))
        .currentStatus(CREATED)
        .originStatus(CREATED)
        .dateTransition(new Date())
        .followingPossibleStates(FOLLOWING_POSSIBLES_STATES)
        .build();
        return List.of(status);
    }

    private boolean doesNotExistsAnyManagementRecord(ManagementRecordEntity managementRecordEntityWithLastCreationDate ){
        return !isExistsAnyManagementRecord(managementRecordEntityWithLastCreationDate);
    }

    private boolean isExistsAnyManagementRecord(ManagementRecordEntity managementRecordEntityWithLastCreationDate ){
        return Objects.nonNull(managementRecordEntityWithLastCreationDate) 
                && Objects.nonNull(managementRecordEntityWithLastCreationDate.getIdentifierNumber())
                && managementRecordEntityWithLastCreationDate.getIdentifierNumber() >= UNO;
    }

    private RosterEntity getRosterEntityByEmail(String email) {
        return rosterRepositoryExecutor.findByEmail(email).orElseThrow(
            () -> new EntityNotFoundException("No roster was found with the email: " + email)
        );
    }
    
    private User completTransitioner(String accessManagementRequesterEmail){
        RosterEntity roster = getRosterEntityByEmail(accessManagementRequesterEmail);
        return User.builder()
        .names(roster.getName())
        .lastnames(roster.getLastname())
        .userPrincipalName(roster.getUserPrincipalName())
        .build();
    }

    private ManagementRecordEntity findFirstByOrderByManagementRecordEntityCreationDateDesc(){
        return managementRecordRepositoryExecutor.findFirstByOrderByManagementRecordEntityCreationDateDesc();
    }
}

package com.jberdeja.idm_authorization.mapper;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.entity.ManagementRecordEntity;
import com.jberdeja.idm_authorization.entity.RosterEntity;
import com.jberdeja.idm_authorization.entity.management_documentation.Status;
import com.jberdeja.idm_authorization.entity.management_documentation.User;
import com.jberdeja.idm_authorization.entity.management_request.AccessManagement;
import com.jberdeja.idm_authorization.repository.ManagementRecordRepository;
import com.jberdeja.idm_authorization.repository.RosterRepository;
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
    private ManagementRecordRepository managementRecordRepository;
    @Autowired
    private RosterRepository rosterRepository;

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
        var managementRecordEntityWithLastCreationDate = managementRecordRepository.findFirstByOrderByManagementRecordCreationDateDesc();
        if (doesNotExistsAnyManagementRecord(managementRecordEntityWithLastCreationDate)) return UNO;
        return managementRecordEntityWithLastCreationDate.getIdentifierNumber() + UNO;
    }

    private User buildManagementRecordCreator(String accessManagementRequesterEmail){
        RosterEntity roster = getRosterByEmail(accessManagementRequesterEmail);
        User managementRecordCreatorUser = new User();
        managementRecordCreatorUser.setEmail(accessManagementRequesterEmail);
        managementRecordCreatorUser.setNames(roster.getName());
        managementRecordCreatorUser.setLastnames(roster.getLastname());
        managementRecordCreatorUser.setUserPrincipalName(roster.getUserPrincipalName());
        return managementRecordCreatorUser;
    }

    private User buildAccessManagementFor(String AccessManagementForUserEmail){
        RosterEntity roster = getRosterByEmail(AccessManagementForUserEmail);
        User accessManagementForUser = new User();
        accessManagementForUser.setEmail(AccessManagementForUserEmail);
        accessManagementForUser.setNames(roster.getName());
        accessManagementForUser.setLastnames(roster.getLastname());
        accessManagementForUser.setUserPrincipalName(roster.getUserPrincipalName());
        return accessManagementForUser;
    } 
    private List<Status> buildTransitions(String accessManagementRequesterEmail){
        Status status = new Status();
        status.setNumber(FIRST_STATUS);
        status.setTransitioner(completTransitioner(accessManagementRequesterEmail));
        status.setCurrentStatus(CREATED);
        status.setOriginStatus(CREATED);
        status.setDateTransition(new Date());
        status.setFollowingPossibleStates(FOLLOWING_POSSIBLES_STATES);
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

    private RosterEntity getRosterByEmail(String email) {
        return rosterRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("No roster was found with the email: " + email));
    }

    private User completTransitioner(String accessManagementRequesterEmail){
        RosterEntity roster = getRosterByEmail(accessManagementRequesterEmail);
        User user = new User();
        user.setNames(roster.getName());
        user.setLastnames(roster.getLastname());
        user.setUserPrincipalName(roster.getUserPrincipalName());
        return user;
    }
}

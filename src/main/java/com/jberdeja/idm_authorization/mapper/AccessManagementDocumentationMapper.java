package com.jberdeja.idm_authorization.mapper;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.entity.AccessManagementDocumentationEntity;
import com.jberdeja.idm_authorization.entity.RosterEntity;
import com.jberdeja.idm_authorization.entity.management_documentation.Status;
import com.jberdeja.idm_authorization.entity.management_documentation.User;
import com.jberdeja.idm_authorization.entity.management_request.AccessManagement;
import com.jberdeja.idm_authorization.repository.AccessManagementDocumentationRepository;
import com.jberdeja.idm_authorization.repository.RosterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessManagementDocumentationMapper {
    private static final String IDM = "IDM-";
    private static final Integer UNO = 1;
    private static final Integer FIRST_STATUS= 1;
    private static final String CREATED = "created";
    private static final List<String> FOLLOWING_POSSIBLES_STATES = List.of("APPROVED", "REJECTED", "CLOSED");
    @Autowired
    private AccessManagementDocumentationRepository accessManagementDocumentationRepository;
    @Autowired
    private RosterRepository rosterRepository;
    public AccessManagementDocumentationEntity map(AccessManagement accessManagement){
        try{
            return mapToAccessManagementDocumentationEntity(accessManagement);
        }catch(Exception e){
            log.error("error mapping the incident", e);
            throw new RuntimeException("Eerror mapping the incident", e);
        }
    }

    private AccessManagementDocumentationEntity mapToAccessManagementDocumentationEntity(AccessManagement accessManagement){
        var accessManagementDocumentationEntity = new AccessManagementDocumentationEntity();
        accessManagementDocumentationEntity.setDocumentationNumber(obtainLastDocumentationNumber());
        accessManagementDocumentationEntity.setDocumentationId(buildDocumentationId(accessManagementDocumentationEntity.getDocumentationNumber()));
        accessManagementDocumentationEntity.setAccessManagementCreator(completeAccessManagementCreator(accessManagement.getAccessManagementRequesterEmail()));
        accessManagementDocumentationEntity.setAccessManagementFor(completeAccessManagementFor(accessManagement.getAccessManagementForUserEmail()));
        accessManagementDocumentationEntity.setTransitions(completeTransitions(accessManagement.getAccessManagementRequesterEmail()));
        Date nowDate = new Date();
        accessManagementDocumentationEntity.setLastUpdateDate(nowDate);
        accessManagementDocumentationEntity.setDocumentCreationDate(nowDate);
        return accessManagementDocumentationEntity;
    }

    private String buildDocumentationId(Integer lastDocumentNumber){
        return IDM + lastDocumentNumber;
    }

    private Integer obtainLastDocumentationNumber(){
        //tener en cuenta para aplicar concurrencia para poder obtener realmete el nuevo numero
        var accessManagementDocumentationWithLastCreationDate = accessManagementDocumentationRepository.findFirstByOrderByDocumentCreationDateDesc();
        if (isNotExistsAnyAccessManagementDocumentation(accessManagementDocumentationWithLastCreationDate)) return UNO;
        return accessManagementDocumentationWithLastCreationDate.getDocumentationNumber();
    }

    private User completeAccessManagementCreator(String accessManagementRequesterEmail){
        RosterEntity roster = getRosterByEmail(accessManagementRequesterEmail);
        User accessManagementRequesterUser = new User();
        accessManagementRequesterUser.setEmail(accessManagementRequesterEmail);
        accessManagementRequesterUser.setNames(roster.getName());
        accessManagementRequesterUser.setLastnames(roster.getLastname());
        accessManagementRequesterUser.setUserPrincipalName(roster.getUserPrincipalName());
        return accessManagementRequesterUser;
    }

    private User completeAccessManagementFor(String AccessManagementForUserEmail){
        RosterEntity roster = getRosterByEmail(AccessManagementForUserEmail);
        User accessManagementForUser = new User();
        accessManagementForUser.setEmail(AccessManagementForUserEmail);
        accessManagementForUser.setNames(roster.getName());
        accessManagementForUser.setLastnames(roster.getLastname());
        accessManagementForUser.setUserPrincipalName(roster.getUserPrincipalName());
        return accessManagementForUser;
    } 
    private List<Status> completeTransitions(String accessManagementRequesterEmail){
        //Status status = transitions.get(FIRST_ELEMENT);
        Status status = new Status();
        status.setNumber(FIRST_STATUS);
        status.setTransitioner(completTransitioner(accessManagementRequesterEmail));
        status.setCurrentStatus(CREATED);
        status.setOriginStatus(CREATED);
        status.setDateTransition(new Date());
        status.setFollowingPossibleStates(FOLLOWING_POSSIBLES_STATES);
        return List.of(status);
    }

    private boolean isNotExistsAnyAccessManagementDocumentation(AccessManagementDocumentationEntity incidentWithLastCreationDate ){
        return !isExistsAnyAccessManagementDocumentation(incidentWithLastCreationDate);
    }

    private boolean isExistsAnyAccessManagementDocumentation(AccessManagementDocumentationEntity accessManagementDocumentationEntity ){
        return Objects.nonNull(accessManagementDocumentationEntity) 
                && Objects.nonNull(accessManagementDocumentationEntity.getDocumentationNumber())
                && accessManagementDocumentationEntity.getDocumentationNumber() >= UNO;
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

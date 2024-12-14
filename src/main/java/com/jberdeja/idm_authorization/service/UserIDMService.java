package com.jberdeja.idm_authorization.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jberdeja.idm_authorization.dto.UserIDMRequest;
import com.jberdeja.idm_authorization.entity.RoleIDMEntity;
import com.jberdeja.idm_authorization.entity.UserIDMEntity;
import com.jberdeja.idm_authorization.repository.RoleIDMRepository;
import com.jberdeja.idm_authorization.repository.UserIDMRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserIDMService {
    @Autowired
    private UserIDMRepository userIDMRepository;
    @Autowired
    private RoleIDMRepository roleIDMRepository;

    @Transactional
    public void createUser(UserIDMRequest userIDMRequest) {
        log.info("Inizilice creation user: " + userIDMRequest);
        UserIDMEntity userIDMEntity = userIDMEntityMapper(userIDMRequest);
        userIDMRepository.save(userIDMEntity);
        log.info("User created ok");
    }
        
    private UserIDMEntity userIDMEntityMapper(UserIDMRequest userIDMRequest) {
        List<RoleIDMEntity> roles = userIDMRequest.getRoles().stream()
                                    .map(this::buildRoleIDMEntity) 
                                    .collect(Collectors.toList()); 

        UserIDMEntity user = buildUserIDMEntity(userIDMRequest, roles);
        log.info("User to create ok ");
        return user;
    }

    private UserIDMEntity buildUserIDMEntity(UserIDMRequest userIDMRequest, List<RoleIDMEntity> roles){
        UserIDMEntity user = new UserIDMEntity();
        user.setEmail(userIDMRequest.getEmail());
        user.setPwd(userIDMRequest.getPassword());
        user.setRoles(roles);
        return user;
    }
    
    private RoleIDMEntity buildRoleIDMEntity(String roleName) {
        RoleIDMEntity roleEntity = roleIDMRepository.findByRoleName(roleName)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        log.info("Roles of database: " + roleEntity);
        return roleEntity;
    }

}

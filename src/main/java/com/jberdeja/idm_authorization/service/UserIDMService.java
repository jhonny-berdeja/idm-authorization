package com.jberdeja.idm_authorization.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entityes.RoleIDMEntity;
import com.jberdeja.idm_authorization.entityes.UserIDMEntity;
import com.jberdeja.idm_authorization.entityes.UserIDMRequest;
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
        List<RoleIDMEntity> roles = new ArrayList<>();
        
        for (String roleName : userIDMRequest.getRoles()) {
            log.info("Role of request: " + roleName);
            RoleIDMEntity roleEntity = buildRoleIDMEntity(roleName);
            roles.add(roleEntity);
        }

        UserIDMEntity user = new UserIDMEntity();
        user.setEmail(userIDMRequest.getEmail());
        user.setPwd(userIDMRequest.getPassword());
        user.setRoles(roles);
        log.info("User to create: " + user);
        return user;
    }

    
    private RoleIDMEntity buildRoleIDMEntity(String roleName) {
        RoleIDMEntity roleEntity = roleIDMRepository.findByRoleName(roleName)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        log.info("Roles of database: " + roleEntity);
        return roleEntity;
    }

}

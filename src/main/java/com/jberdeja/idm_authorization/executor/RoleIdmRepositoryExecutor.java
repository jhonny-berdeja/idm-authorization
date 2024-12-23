package com.jberdeja.idm_authorization.executor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.entity.RoleIdmEntity;
import com.jberdeja.idm_authorization.exception.PostgresSqlDatabaseIdmException;
import com.jberdeja.idm_authorization.repository.RoleIdmRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RoleIdmRepositoryExecutor {
    @Autowired
    private RoleIdmRepository roleIdmRepository;

    public Optional<RoleIdmEntity> findByRoleName(String roleName){
        try{
            return roleIdmRepository.findByRoleName(roleName);
        } catch (DataAccessException e) {
            log.error("error querying role idm by name from postgressql database: " + roleName, e);
            throw new PostgresSqlDatabaseIdmException("error querying role idm by name from postgressql database: " + roleName, e);
        } catch (Exception e) {
            log.error("Unexpected error querying role idm by name from postgressql database: " + roleName, e);
            throw new PostgresSqlDatabaseIdmException("Unexpected error querying role idm by name from postgressql database: " + roleName, e);
        }
    }
}

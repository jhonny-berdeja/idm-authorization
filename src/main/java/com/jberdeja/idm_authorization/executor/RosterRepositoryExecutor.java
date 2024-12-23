package com.jberdeja.idm_authorization.executor;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.entity.RosterEntity;
import com.jberdeja.idm_authorization.exception.PostgresSqlDatabaseIdmException;
import com.jberdeja.idm_authorization.repository.RosterRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RosterRepositoryExecutor {

    @Autowired
    private RosterRepository rosterRepository;

    public Optional<RosterEntity> findByEmail(String email){
        try{
            return rosterRepository.findByEmail(email);
        }catch(DataAccessException e){
            log.error("error when querying the roster postgressql database by email: " + email, e);
            throw new PostgresSqlDatabaseIdmException("error when querying the roster postgressql database by email: " + email, e);
        }catch(Exception e){
            log.error("unexpected error querying roster postgressql database via email: " + email, e);
            throw new PostgresSqlDatabaseIdmException("unexpected error querying roster postgressql database via email: " + email, e);
        }
    }
}

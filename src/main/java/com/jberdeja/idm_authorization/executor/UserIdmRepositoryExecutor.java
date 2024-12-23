package com.jberdeja.idm_authorization.executor;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;
import com.jberdeja.idm_authorization.exception.PostgresSqlDatabaseIdmException;
import com.jberdeja.idm_authorization.repository.UserIdmRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserIdmRepositoryExecutor {

    @Autowired
    private UserIdmRepository userIdmRepository;

    public Optional<UserIdmEntity> findByEmail(String username){
        try{
            return userIdmRepository.findByEmail(username);
        } catch (DataAccessException e) {
            log.error("error when querying user by email in postgressql database: " + username, e);
            throw new PostgresSqlDatabaseIdmException("error when querying user by email in postgressql database: " + username, e);
        } catch (Exception e) {
            log.error("unexpected error when querying user by email in postgressql database: " + username, e);
            throw new PostgresSqlDatabaseIdmException("unexpected error when querying user by email in postgressql database: " + username, e);
        }
    }

    public void save(UserIdmEntity userIdmEntity){
        try{
            saveUser(userIdmEntity);
        } catch (DataAccessException e) {
            log.error("error saving user idm in postgressql database", e);
            throw new PostgresSqlDatabaseIdmException("error saving user idm in postgressql database", e);
        } catch (Exception e) {
            log.error("unexpected error saving user idm in postgressql database", e);
            throw new PostgresSqlDatabaseIdmException("unexpected error saving user idm in postgressql database", e);
        }
    }
    
    @Transactional
    private void saveUser(UserIdmEntity userIdmEntity) {
        userIdmRepository.save(userIdmEntity);
    }
}

package com.jberdeja.idm_authorization.executor;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.exception.MongoDatabaseIdmException;
import com.jberdeja.idm_authorization.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationRepositoryExecutor {
    @Autowired
    private ApplicationRepository applicationRepository;

    public ApplicationEntity save(ApplicationEntity applicationEntity){
        try {
            return saveApplication(applicationEntity);
        } catch (DataAccessException e) {
            log.error("Error accessing the database while saving ApplicationEntity", e);
            throw new MongoDatabaseIdmException("Database error occurred while saving ApplicationEntity", e);
        }catch (Exception e) {
            log.error("Unexpected error while saving ApplicationEntity", e);
            throw new MongoDatabaseIdmException("Unexpected error occurred while saving ApplicationEntity", e);
        }
    }

    public ApplicationEntity findByName(String applicationName){
        try {
            return applicationRepository.findByName(applicationName);
        } catch (DataAccessException e) {
            log.error("Mongodb database error when searching application by name: " + applicationName, e);
            throw new MongoDatabaseIdmException("Mongodb database error when searching application by name: " + applicationName, e);
        } catch (Exception e) {
            log.error("Unexpected error when searching application by name in mongodb database: " + applicationName, e);
            throw new MongoDatabaseIdmException("Unexpected error when searching application by name in mongodb database: " + applicationName, e);
        }
    }

    public List<ApplicationEntity> findAll(){
        try {
            return applicationRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Mongodb database error retrieving all applications", e);
            throw new MongoDatabaseIdmException("Mongodb database error retrieving all applications", e);
        }catch (Exception e) {
            log.error("Unexpected error retrieving all applications from mongodb database", e);
            throw new MongoDatabaseIdmException("Unexpected error retrieving all applications from mongodb database", e);
        }
    }

    @Transactional
    private ApplicationEntity saveApplication(ApplicationEntity applicationEntity) {
        return applicationRepository.save(applicationEntity);
    }
}

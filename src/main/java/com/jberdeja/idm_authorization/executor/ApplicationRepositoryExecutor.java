package com.jberdeja.idm_authorization.executor;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
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
            log.info("Application name: " + applicationEntity.getName());
            return saveApplication(applicationEntity);
        } catch(DataAccessResourceFailureException e){
            log.error("connection error to database when saving application", e);
            throw new MongoDatabaseIdmException("connection error to database when saving application", e);
        } catch(QueryTimeoutException e){
            log.error("time out error in database when saving application", e);
            throw new MongoDatabaseIdmException("time out error in database when saving application", e);
        }catch (DataAccessException e) {
            log.error("database access error when saving application", e);
            throw new MongoDatabaseIdmException("database access error when saving application", e);
        }catch (Exception e) {
            log.error("unexpected error in database when saving application", e);
            throw new MongoDatabaseIdmException("unexpected error in database when saving application", e);
        }
    }

    public ApplicationEntity findByName(String applicationName){
        try {
            log.info("Application name: " + applicationName);
            return applicationRepository.findByName(applicationName);
        } catch(DataAccessResourceFailureException e){
            log.error("database connection error when searching for application by name", e);
            throw new MongoDatabaseIdmException("database connection error when searching for application by name", e);
        } catch(QueryTimeoutException e){
            log.error("time out error in database when searching for application by name", e);
            throw new MongoDatabaseIdmException("time out error in database when searching for application by name", e);
        } catch (DataAccessException e) {
            log.error("database access error when searching for application by name", e);
            throw new MongoDatabaseIdmException("database access error when searching for application by name", e);
        } catch (Exception e) {
            log.error("unexpected database error when searching for application by name", e);
            throw new MongoDatabaseIdmException("unexpected database error when searching for application by name", e);
        }
    }

    public List<ApplicationEntity> findAll(){
        try {
            return applicationRepository.findAll();
        }  catch(DataAccessResourceFailureException e){
            log.error("database connection error when searching for all applications", e);
            throw new MongoDatabaseIdmException("database connection error when searching for all applications", e);
        } catch(QueryTimeoutException e){
            log.error("database time out error when searching for all applications", e);
            throw new MongoDatabaseIdmException("database time out error when searching for all applications", e);
        } catch (DataAccessException e) {
            log.error("database access error when searching for all applications", e);
            throw new MongoDatabaseIdmException("database access error when searching for all applications", e);
        }catch (Exception e) {
            log.error("unexpected database error when searching all applications", e);
            throw new MongoDatabaseIdmException("unexpected database error when searching all applications", e);
        }
    }

    public boolean existsByName(String applicationName){
        try{
            log.info("Application name: " + applicationName);
            return applicationRepository.existsByName(applicationName);
        }  catch(DataAccessResourceFailureException e){
            log.error("database connection error when querying application existence by name", e);
            throw new MongoDatabaseIdmException("database connection error when querying application existence by name", e);
        } catch(QueryTimeoutException e){
            log.error("database time out error when querying application existence by name", e);
            throw new MongoDatabaseIdmException("database time out error when querying application existence by name", e);
        } catch (DataAccessException e) {
            log.error("database access error when querying application existence by name", e);
            throw new MongoDatabaseIdmException("database access error when querying application existence by name", e);
        }catch (Exception e) {
            log.error("unexpected database error when querying application existence by name", e);
            throw new MongoDatabaseIdmException("unexpected database error when querying application existence by name", e);
        }
    }

    public boolean doesNotExistByName(String applicationName){
        try{
            log.info("Application name: " + applicationName);
            return applicationRepository.doesNotExistByName(applicationName);
        }  catch(DataAccessResourceFailureException e){
            log.error("database connection error when querying non-existence of application by name", e);
            throw new MongoDatabaseIdmException("database connection error when querying non-existence of application by name", e);
        } catch(QueryTimeoutException e){
            log.error("database time out error when querying non-existence of application by name", e);
            throw new MongoDatabaseIdmException("database time out error when querying non-existence of application by name", e);
        } catch (DataAccessException e) {
            log.error("database access error when querying non-existence of application by name", e);
            throw new MongoDatabaseIdmException("database access error when querying non-existence of application by name", e);
        }catch (Exception e) {
            log.error("unexpected database error when querying non-existence of application by name", e);
            throw new MongoDatabaseIdmException("unexpected database error when querying non-existence of application by name", e);
        }
    }

    @Transactional
    private ApplicationEntity saveApplication(ApplicationEntity applicationEntity) {
        return applicationRepository.save(applicationEntity);
    }
}

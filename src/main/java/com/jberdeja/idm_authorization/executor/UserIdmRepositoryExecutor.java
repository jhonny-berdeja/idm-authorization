package com.jberdeja.idm_authorization.executor;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
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
            log.info("username: " + username);
            return userIdmRepository.findByEmail(username);
        }  catch(DataAccessResourceFailureException e){
            log.error("database connection error consulting by email", e);
            throw new PostgresSqlDatabaseIdmException("database connection error consulting by email", e);
        } catch(QueryTimeoutException e){
            log.error("database time out error consulting by email", e);
            throw new PostgresSqlDatabaseIdmException("database time out error consulting by email", e);
        } catch (DataAccessException e) {
            log.error("database access error consulting by email", e);
            throw new PostgresSqlDatabaseIdmException("database access error consulting by email", e);
        }catch (Exception e) {
            log.error("unexpected database error querying by email", e);
            throw new PostgresSqlDatabaseIdmException("unexpected database error querying by email", e);
        }
    }

    public void save(UserIdmEntity userIdmEntity){
        try{
            log.info("username: " + userIdmEntity.getEmail());
            saveUser(userIdmEntity);
        }  catch(DataAccessResourceFailureException e){
            log.error("database connection error saving user", e);
            throw new PostgresSqlDatabaseIdmException("database connection error saving user", e);
        } catch(QueryTimeoutException e){
            log.error("database time out error saving user", e);
            throw new PostgresSqlDatabaseIdmException("database time out error saving user", e);
        } catch (DataAccessException e) {
            log.error("database access error saving user", e);
            throw new PostgresSqlDatabaseIdmException("database access error saving user", e);
        }catch (Exception e) {
            log.error("unexpected error database saving user", e);
            throw new PostgresSqlDatabaseIdmException("unexpected error database saving user", e);
        }
    }

    public boolean existsByEmail(String email){
        try{
            log.info("email: " + email);
            return userIdmRepository.existsByEmail(email);
        }  catch(DataAccessResourceFailureException e){
            log.error("database connection error checking existence by email", e);
            throw new PostgresSqlDatabaseIdmException("database connection error checking existence by email", e);
        } catch(QueryTimeoutException e){
            log.error("time out error in database checking existence by email", e);
            throw new PostgresSqlDatabaseIdmException("time out error in database checking existence by email", e);
        } catch (DataAccessException e) {
            log.error("database access error checking existence by email", e);
            throw new PostgresSqlDatabaseIdmException("database access error checking existence by email", e);
        }catch (Exception e) {
            log.error("unexpected database error checking existence by email", e);
            throw new PostgresSqlDatabaseIdmException("unexpected database error checking existence by email", e);
        }
    }
    
    @Transactional
    private void saveUser(UserIdmEntity userIdmEntity) {
        userIdmRepository.save(userIdmEntity);
    }
}

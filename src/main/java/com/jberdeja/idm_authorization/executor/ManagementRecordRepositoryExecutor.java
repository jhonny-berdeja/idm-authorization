package com.jberdeja.idm_authorization.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entity.ManagementRecordEntity;
import com.jberdeja.idm_authorization.exception.MongoDatabaseIdmException;
import com.jberdeja.idm_authorization.repository.ManagementRecordRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ManagementRecordRepositoryExecutor {
    @Autowired
    private ManagementRecordRepository managementRecordRepository;

    public ManagementRecordEntity findFirstByOrderByManagementRecordEntityCreationDateDesc() {
        try {
            return managementRecordRepository.findFirstByOrderByManagementRecordCreationDateDesc();
        } catch (DataAccessException e) {
            log.error("error when querying the management record with last creation date to the mongo database", e);
            throw new MongoDatabaseIdmException("error when querying the management record with last creation date to the mongo database", e);
        } catch (Exception e) {
            log.error("Unexpected error while querying management log with last creation date to mongo database", e);
            throw new MongoDatabaseIdmException("Unexpected error while querying management log with last creation date to mongo database", e);
        }
    }

    public ManagementRecordEntity save(ManagementRecordEntity managementRecordEntity){
        try{
            return saveManagementRecord(managementRecordEntity);
        }catch(DataAccessException e){
            log.error("error when saving the management reord in the nomgodb database", e);
            throw new MongoDatabaseIdmException("error when saving the management reord in the nomgodb database", e);
        } catch (Exception e) {
            log.error("Unexpected error when saving management log to nomgodb database", e);
            throw new MongoDatabaseIdmException("Unexpected error when saving management log to nomgodb database", e);
        }
    }

    @Transactional
    private ManagementRecordEntity saveManagementRecord(ManagementRecordEntity managementRecordEntity) {
        return managementRecordRepository.save(managementRecordEntity);
    }
}

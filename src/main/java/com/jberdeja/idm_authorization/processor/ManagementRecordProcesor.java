package com.jberdeja.idm_authorization.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entity.ManagementRecordEntity;
import com.jberdeja.idm_authorization.entity.management_request.AccessManagement;
import com.jberdeja.idm_authorization.executor.ManagementRecordRepositoryExecutor;
import com.jberdeja.idm_authorization.mapper.ManagementRecordMapper;

@Component
public class ManagementRecordProcesor {
    @Autowired
    private ManagementRecordMapper managementRecordMapper;
    @Autowired
    private ManagementRecordRepositoryExecutor managementRecordRepositoryExecutor;

    @Transactional
    public ManagementRecordEntity register(AccessManagement accessManagement){
        var managementRecordEntity = mapToManagementRecordEntity(accessManagement);
        return managementRecordRepositoryExecutor.save(managementRecordEntity);
    }

    private ManagementRecordEntity mapToManagementRecordEntity(
        AccessManagement accessManagement
    ){

        return managementRecordMapper.mapToManagementRecordEntity(accessManagement);
    }
 
}

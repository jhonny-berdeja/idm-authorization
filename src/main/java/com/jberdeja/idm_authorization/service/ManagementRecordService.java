package com.jberdeja.idm_authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entity.ManagementRecordEntity;
import com.jberdeja.idm_authorization.entity.management_request.AccessManagement;
import com.jberdeja.idm_authorization.executor.ManagementRecordRepositoryExecutor;
import com.jberdeja.idm_authorization.mapper.ManagementRecordMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagementRecordService {
    @Autowired
    private ManagementRecordMapper managementRecordMapper;
    @Autowired
    private ManagementRecordRepositoryExecutor managementRecordRepositoryExecutor;

    @Transactional
    public ManagementRecordEntity register(AccessManagement accessManagement){
        ManagementRecordEntity managementRecordEntity = managementRecordMapper.mapToManagementRecordEntity(accessManagement);
        return managementRecordRepositoryExecutor.save(managementRecordEntity);
    }

 
}

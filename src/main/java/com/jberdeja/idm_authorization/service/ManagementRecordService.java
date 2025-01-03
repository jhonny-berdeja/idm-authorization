package com.jberdeja.idm_authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entity.ManagementRecordEntity;
import com.jberdeja.idm_authorization.entity.management_request.AccessManagement;
import com.jberdeja.idm_authorization.processor.ManagementRecordProcesor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagementRecordService {
    @Autowired
    private ManagementRecordProcesor managementRecordProcesor;

    @Transactional
    public ManagementRecordEntity register(AccessManagement accessManagement){
        log.info("starting management record");
        var managementRecordEntity = managementRecordProcesor.register(accessManagement);
        log.info("management registration completed correctly");
        return managementRecordEntity;
    }
 
}

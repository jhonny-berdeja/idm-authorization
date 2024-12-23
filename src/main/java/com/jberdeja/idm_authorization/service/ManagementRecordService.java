package com.jberdeja.idm_authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entity.ManagementRecordEntity;
import com.jberdeja.idm_authorization.entity.management_request.AccessManagement;
import com.jberdeja.idm_authorization.mapper.ManagementRecordMapper;
import com.jberdeja.idm_authorization.repository.ManagementRecordRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagementRecordService {

    @Autowired
    private ManagementRecordRepository incidenceRepository;
    @Autowired
    private ManagementRecordMapper managementRecordMapper;

    @Transactional
    public ManagementRecordEntity register(AccessManagement accessManagement){
        try{
            ManagementRecordEntity mappedIncidence = managementRecordMapper.mapToManagementRecordEntity(accessManagement);
            return incidenceRepository.save(mappedIncidence);
        }catch(Exception e){
            log.error("Error creating incident", e);
            throw new RuntimeException("Error creating incident", e);
        }
    }

 
}

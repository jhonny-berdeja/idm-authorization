package com.jberdeja.idm_authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entity.AccessManagementDocumentationEntity;
import com.jberdeja.idm_authorization.entity.management_request.AccessManagement;
import com.jberdeja.idm_authorization.mapper.AccessManagementDocumentationMapper;
import com.jberdeja.idm_authorization.repository.AccessManagementDocumentationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccessManagementDocumentationService {

    @Autowired
    private AccessManagementDocumentationRepository incidenceRepository;
    @Autowired
    private AccessManagementDocumentationMapper accessManagementDocumentationMapper;

    @Transactional
    public AccessManagementDocumentationEntity document(AccessManagement accessManagement){
        try{
            AccessManagementDocumentationEntity mappedIncidence = accessManagementDocumentationMapper.map(accessManagement);
            return incidenceRepository.save(mappedIncidence);
        }catch(Exception e){
            log.error("Error creating incident", e);
            throw new RuntimeException("Error creating incident", e);
        }
    }

 
}

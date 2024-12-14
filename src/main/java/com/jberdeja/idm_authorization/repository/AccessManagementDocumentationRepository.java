package com.jberdeja.idm_authorization.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jberdeja.idm_authorization.entity.AccessManagementDocumentationEntity;

@Repository
public interface AccessManagementDocumentationRepository extends MongoRepository<AccessManagementDocumentationEntity, Long>{
    AccessManagementDocumentationEntity findFirstByOrderByDocumentCreationDateDesc();
}

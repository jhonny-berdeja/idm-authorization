package com.jberdeja.idm_authorization.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jberdeja.idm_authorization.entity.ManagementRecordEntity;

@Repository
public interface ManagementRecordRepository extends MongoRepository<ManagementRecordEntity, Long>{
    ManagementRecordEntity findFirstByOrderByManagementRecordCreationDateDesc();
}

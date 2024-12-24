package com.jberdeja.idm_authorization.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;

@Repository
public interface ApplicationRepository extends MongoRepository<ApplicationEntity, Long>{
     ApplicationEntity findByName(String name);
     boolean existsByName(String name); 
     default boolean doesNotExistByName(String name) {
          return ! existsByName(name);
      }
}

package com.jberdeja.idm_authorization.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;

@Repository
public interface UserIdmRepository extends CrudRepository<UserIdmEntity, Long>{
    Optional<UserIdmEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}

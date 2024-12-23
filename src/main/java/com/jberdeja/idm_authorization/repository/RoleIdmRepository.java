package com.jberdeja.idm_authorization.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.jberdeja.idm_authorization.entity.RoleIdmEntity;

@Repository
public interface RoleIdmRepository extends CrudRepository<RoleIdmEntity, Long>{
    Optional<RoleIdmEntity> findByRoleName(String roleName);
}

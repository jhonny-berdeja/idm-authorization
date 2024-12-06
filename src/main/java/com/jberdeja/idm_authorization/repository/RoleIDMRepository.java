package com.jberdeja.idm_authorization.repository;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.jberdeja.idm_authorization.entityes.RoleIDMEntity;

@Repository
public interface RoleIDMRepository extends CrudRepository<RoleIDMEntity, Long>{
    Optional<RoleIDMEntity> findByRoleName(String roleName);
}

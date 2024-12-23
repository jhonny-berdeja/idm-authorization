package com.jberdeja.idm_authorization.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.jberdeja.idm_authorization.entity.RosterEntity;

@Repository
public interface RosterRepository extends CrudRepository<RosterEntity, Long>{
    Optional<RosterEntity> findByEmail(String email);
}

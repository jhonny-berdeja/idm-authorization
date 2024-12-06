package com.jberdeja.idm_authorization.repository;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.jberdeja.idm_authorization.entityes.UserIDMEntity;

@Repository
public interface UserIDMRepository extends CrudRepository<UserIDMEntity, Long>{
    Optional<UserIDMEntity> findByEmail(String email);
}

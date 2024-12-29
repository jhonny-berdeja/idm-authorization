package com.jberdeja.idm_authorization.repository;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import com.jberdeja.idm_authorization.buider.EntityBuilder;
import com.jberdeja.idm_authorization.config.UnitTestWithPostgrsSql;
import com.jberdeja.idm_authorization.entity.RoleIdmEntity;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;

public class UserIdmRepositoryTest extends UnitTestWithPostgrsSql{
    @Autowired
    private UserIdmRepository userIdmRepository;
    @Autowired
    private RoleIdmRepository roleIdmRepository;
    
    @Test
    void save_happyCase_ok(){
 
    RoleIdmEntity roleIdmEntity = EntityBuilder.buildRoleIdmEntity();

    RoleIdmEntity roleIdmEntitySaved = roleIdmRepository.save(roleIdmEntity);

    assertThat(roleIdmEntitySaved).isNotNull();
    assertThat(roleIdmEntitySaved.getRoleName()).isEqualTo(roleIdmEntity.getRoleName());

    UserIdmEntity userIdmEntity = EntityBuilder.buildUserIdmEntity();
    userIdmEntity.setRoles(List.of(roleIdmEntitySaved));

    UserIdmEntity savedUser = userIdmRepository.save(userIdmEntity);

    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getRoles()).hasSize(1);
    }
}

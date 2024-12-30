package com.jberdeja.idm_authorization.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import com.jberdeja.idm_authorization.buider.EntityBuilder;
import com.jberdeja.idm_authorization.config.UnitTestWithPostgrsSql;
import com.jberdeja.idm_authorization.entity.RoleIdmEntity;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RoleIdmRepositoryTest  extends UnitTestWithPostgrsSql{

    @Autowired
    private RoleIdmRepository roleIdmRepository;
    
    @Test
    void save_happyCase_ok(){
        RoleIdmEntity roleIdmEntity = EntityBuilder.buildRoleIdmEntity();

        RoleIdmEntity roleIdmEntitySaved = roleIdmRepository.save(roleIdmEntity);

        String roleName = roleIdmEntity.getRoleName();
    
        assertThat(roleIdmEntitySaved).isNotNull();
        assertThat(roleIdmEntitySaved.getRoleName()).isEqualTo(roleName);
    }

}

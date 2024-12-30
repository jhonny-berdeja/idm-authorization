package com.jberdeja.idm_authorization.repository;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import com.jberdeja.idm_authorization.buider.EntityBuilder;
import com.jberdeja.idm_authorization.config.UnitTestWithPostgrsSql;
import com.jberdeja.idm_authorization.entity.RoleIdmEntity;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserIdmRepositoryTest extends UnitTestWithPostgrsSql{
    @Autowired
    private UserIdmRepository userIdmRepository;
    @Autowired
    private RoleIdmRepository roleIdmRepository;
    
    @Test
    void save_happyCase_ok(){
    RoleIdmEntity roleIdmEntity = saveAnyRoleIdmEntity();
    UserIdmEntity userIdmEntity = buildUserIdmEntity(roleIdmEntity);
    UserIdmEntity savedUser = userIdmRepository.save(userIdmEntity);

    String email = userIdmEntity.getEmail();

    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getRoles()).hasSize(1);
    assertThat(savedUser.getEmail()).isEqualTo(email);
    }

    @Test
    void findByEmail_happyCase_ok(){
        RoleIdmEntity roleIdmEntity = saveAnyRoleIdmEntity();
        UserIdmEntity userIdmEntity = buildUserIdmEntity(roleIdmEntity);
        UserIdmEntity savedUser = saveUserIdmEntity(userIdmEntity);

        String email = savedUser.getEmail();

        Optional<UserIdmEntity> userEntityFind = userIdmRepository.findByEmail(email); 

        assertThat(userEntityFind.get()).isNotNull();
        assertThat(userEntityFind.get().getEmail()).isEqualTo(email);
    }

    @Test
    void existsByEmail_happyCase_ok(){
        RoleIdmEntity roleIdmEntity = saveAnyRoleIdmEntity();
        UserIdmEntity userIdmEntity = buildUserIdmEntity(roleIdmEntity);
        UserIdmEntity savedUser = saveUserIdmEntity(userIdmEntity);

        String email = savedUser.getEmail();

        boolean existsUser = userIdmRepository.existsByEmail(email);

        assertThat(existsUser).isTrue();
    }

    private RoleIdmEntity saveAnyRoleIdmEntity(){
        RoleIdmEntity roleIdmEntity = EntityBuilder.buildRoleIdmEntity();

        RoleIdmEntity roleIdmEntitySaved = roleIdmRepository.save(roleIdmEntity);

        String roleName = roleIdmEntity.getRoleName();
    
        assertThat(roleIdmEntitySaved).isNotNull();
        assertThat(roleIdmEntitySaved.getRoleName()).isEqualTo(roleName);

        return roleIdmEntitySaved;
    }

    private UserIdmEntity buildUserIdmEntity(RoleIdmEntity roleIdmEntity){
        UserIdmEntity userIdmEntity = EntityBuilder.buildUserIdmEntity();
        userIdmEntity.setRoles(List.of(roleIdmEntity));

        return userIdmEntity;
    }

    private UserIdmEntity saveUserIdmEntity(UserIdmEntity userIdmEntity){
        UserIdmEntity savedUser = userIdmRepository.save(userIdmEntity);
    
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRoles()).hasSize(1);

        return savedUser;
    }
}

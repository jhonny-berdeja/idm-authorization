package com.jberdeja.idm_authorization.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import com.jberdeja.idm_authorization.buider.EntityBuilder;
import com.jberdeja.idm_authorization.config.UnitTestWithPostgrsSql;
import com.jberdeja.idm_authorization.entity.RosterEntity;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RosterRepositoryTest extends UnitTestWithPostgrsSql{
    @Autowired
    private RosterRepository rosterRepository;

    @Test
    void findByEmail_hppyCase_ok(){
        RosterEntity rosterEntity = EntityBuilder.buildRosterEntity();
        String email = rosterEntity.getEmail();
        RosterEntity rosterEntitySaved = rosterRepository.save(rosterEntity);

        assertThat(rosterEntitySaved).isNotNull();
        assertThat(rosterEntitySaved.getEmail()).isEqualTo(email);
        
        Optional<RosterEntity> rosterEntityFind = rosterRepository.findByEmail(email);

        assertThat(rosterEntityFind.isPresent()).isTrue();
        assertThat(rosterEntityFind.get().getEmail()).isEqualTo(email);

    }
}

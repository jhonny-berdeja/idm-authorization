package com.jberdeja.idm_authorization.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import com.jberdeja.idm_authorization.buider.EntityBuilder;
import com.jberdeja.idm_authorization.config.UnitTestWithMongoDb;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ManagementRecordRepositoryTest extends UnitTestWithMongoDb{

    @Autowired
    private ManagementRecordRepository managementRecordRepository;

    @Test
    void save_happyCase_ok(){
        var managementRecordEntity = EntityBuilder.buildManagementRecordEntity();
        String identifier = managementRecordEntity.getIdentifier();

        var entitySaved = managementRecordRepository.save(managementRecordEntity);

        assertThat(entitySaved).isNotNull();
        assertThat(entitySaved.getIdentifier()).isEqualTo(identifier);
    }
}

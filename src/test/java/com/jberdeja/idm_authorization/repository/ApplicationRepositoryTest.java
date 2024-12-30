package com.jberdeja.idm_authorization.repository;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import static org.assertj.core.api.Assertions.assertThat;
import com.jberdeja.idm_authorization.buider.EntityBuilder;
import com.jberdeja.idm_authorization.config.UnitTestWithMongoDb;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ApplicationRepositoryTest extends UnitTestWithMongoDb{

    @Autowired
    private ApplicationRepository applicationRepository;
 

    @Test
    void save_happyCase_ok(){
        ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();
        ApplicationEntity applicationEntityCreated = applicationRepository.save(applicationEntity);
        assertThat(applicationEntityCreated).isNotNull();
        assertThat(applicationEntityCreated.getName()).isEqualTo(applicationEntity.getName());
    }

    @Test
    void getApplicationByName_happyCase_ok(){
        ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();
        String applicationName = applicationEntity.getName();
        ApplicationEntity applicationEntityCreated = applicationRepository.save(applicationEntity); 
        assertThat(applicationEntityCreated).isNotNull();
        assertThat(applicationEntityCreated.getName()).isEqualTo(applicationEntity.getName());
        ApplicationEntity applicationEntityFind = applicationRepository.findByName(applicationName);
        assertThat(applicationEntityFind).isNotNull();
        assertThat(applicationEntityFind.getName()).isEqualTo(applicationName);
    }

    @Test
    void getApplications_happyCase_ok(){
        ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();
        ApplicationEntity applicationEntityCreated = applicationRepository.save(applicationEntity); 
        assertThat(applicationEntityCreated).isNotNull();
        List<ApplicationEntity> applications = applicationRepository.findAll(); 
        assertThat(applications.size()).isEqualTo(1);
    }
}

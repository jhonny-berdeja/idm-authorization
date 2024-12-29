package com.jberdeja.idm_authorization.repository;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import com.jberdeja.idm_authorization.buider.EntityBuilder;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;

@Testcontainers
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) 
public class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");
    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUpByMethod() {
        mongoTemplate.getDb().drop();
    } 

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.waitingFor(Wait.forListeningPort());
        mongoDBContainer.withStartupTimeout(Duration.ofSeconds(1));
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

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

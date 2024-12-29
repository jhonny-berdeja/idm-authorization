package com.jberdeja.idm_authorization.service;

import static org.mockito.ArgumentMatchers.any;
import java.util.List;
import org.junit.jupiter.api.Nested;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import static org.assertj.core.api.Assertions.assertThat;
import com.jberdeja.idm_authorization.buider.EntityBuilder;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.exception.MongoDatabaseIdmException;
import com.jberdeja.idm_authorization.repository.ApplicationRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

@Testcontainers
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) 
public class ApplicationServiceTest {
    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");
    @Autowired
    private MongoTemplate mongoTemplate;
    
/*     @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
 */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
/*         registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver"); */
    }

    @BeforeEach
    void setUpByMethod() {
        mongoTemplate.getDb().drop();
    } 

    @Autowired
    private ApplicationService applicationService;

    @Test
    void createApplication_happyCase_ok() {
        String name = "Application name 1";
        ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();
        applicationEntity.setName(name);

        ApplicationEntity savedApplicationEntity = applicationService.createApplication(applicationEntity);

        assertThat(savedApplicationEntity).isNotNull();
        assertThat(savedApplicationEntity.getName()).isEqualTo(name);

    }

    @Test
    void createApplication_alreadyExists_IllegalArgumentException() {
        String name = "Application name 1";
        ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();
        applicationEntity.setName(name);

        ApplicationEntity savedApplicationEntity = applicationService.createApplication(applicationEntity);

        assertThat(savedApplicationEntity).isNotNull();
        assertThat(savedApplicationEntity.getName()).isEqualTo(name);

         assertThatThrownBy(() -> applicationService.createApplication(applicationEntity))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("this application already exists");
    }

    @Test
    void createApplication_nameIsEmpty_IllegalArgumentException() {
        String nameAmpty = "";
        ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();
        applicationEntity.setName(nameAmpty);

        assertThatThrownBy(() -> applicationService.createApplication(applicationEntity))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("error the application name is null or blank");
    }

    @Test
    void createApplication_nameIsNull_IllegalArgumentException() {
        String nameNull = null;
        ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();
        applicationEntity.setName(nameNull);

        assertThatThrownBy(() -> applicationService.createApplication(applicationEntity))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("error the application name is null or blank");
    }

    @Test
    void getApplications_happyCase_ok(){
        
        ApplicationEntity applicationEntity1 = EntityBuilder.buildApplicationEntity();
        applicationEntity1.setName("App 1");
        ApplicationEntity applicationEntity2 = EntityBuilder.buildApplicationEntity();
        applicationEntity2.setName("App 2");

        applicationService.createApplication(applicationEntity1);
        applicationService.createApplication(applicationEntity2);

        List<String> applicationExpectations = List.of(applicationEntity1.getName(), applicationEntity2.getName());

        List<String> applications = applicationService.getApplications();

        assertThat(applications).isNotNull();
        assertThat(applications).isEqualTo(applicationExpectations);
    }

    @Test
    void getApplicationByName_happyCase_ok(){
        ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();
        String name = applicationEntity.getName();
        applicationService.createApplication(applicationEntity);
        
        ApplicationEntity applicationEntityFoud = applicationService.getApplicationByName(name);

        assertThat(applicationEntityFoud).isNotNull();
        assertThat(applicationEntityFoud.getName()).isEqualTo(name);
    }

    @Test
    void getApplicationByName_nameIsEmpty_IllegalArgumentException(){
        String nameEmpty = "";
        assertThatThrownBy(() -> applicationService.getApplicationByName(nameEmpty))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("error the application name is null or blank");
    }

    @Test
    void getApplicationByName_nameIsNull_IllegalArgumentException(){
        String nameNull = null;
        assertThatThrownBy(() -> applicationService.getApplicationByName(nameNull))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("error the application name is null or blank");
    }

    @Nested
    class MockedTests {

        @MockitoBean
        private ApplicationRepository applicationRepository;
        @Autowired
        private ApplicationService applicationService;
    
        @Test
        void createApplication_databaseConnectionError_MongoDatabaseIdmException() {
 
            Mockito.when(applicationRepository.save(any(ApplicationEntity.class)))
                .thenThrow(new DataAccessResourceFailureException("Database connection error"));

            ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();

            assertThatThrownBy(() -> applicationService.createApplication(applicationEntity))
                .isInstanceOf(MongoDatabaseIdmException.class)
                .hasMessageContaining("connection error to database when saving application");
        }

        @Test
        void createApplication_databaseTimeoutError_MongoDatabaseIdmException() {
 
            Mockito.when(applicationRepository.save(any(ApplicationEntity.class)))
                .thenThrow(new QueryTimeoutException("Database connection error"));

            ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();

            assertThatThrownBy(() -> applicationService.createApplication(applicationEntity))
                .isInstanceOf(MongoDatabaseIdmException.class)
                .hasMessageContaining("time out error in database when saving application");
        }

        @Test
        void getApplications_databaseConnectionError_MongoDatabaseIdmException(){
            Mockito.when(applicationRepository.findAll()).thenThrow(new DataAccessResourceFailureException("Database connection error"));

            assertThatThrownBy(() -> applicationService.getApplications())
            .isInstanceOf(MongoDatabaseIdmException.class)
            .hasMessageContaining("database connection error when searching for all applications");
        }

        @Test
        void getApplications_databaseTimeoutError_MongoDatabaseIdmException(){
            Mockito.when(applicationRepository.findAll()).thenThrow(new QueryTimeoutException("Database connection error"));

            assertThatThrownBy(() -> applicationService.getApplications())
            .isInstanceOf(MongoDatabaseIdmException.class)
            .hasMessageContaining("database time out error when searching for all applications");
        }

        @Test
        void getApplicationByName_databaseConnectionError_MongoDatabaseIdmException(){
            String name = "App";
            Mockito.when(applicationRepository.doesNotExistByName(any())).thenThrow(new DataAccessResourceFailureException("Database connection error"));

            assertThatThrownBy(() -> applicationService.getApplicationByName(name))
            .isInstanceOf(MongoDatabaseIdmException.class)
            .hasMessageContaining("database connection error when querying non-existence of application by name");
        }

        @Test
        void getApplicationByName_databaseTimeoutError_MongoDatabaseIdmException(){
            String name = "App";
            Mockito.when(applicationRepository.doesNotExistByName(any())).thenThrow(new QueryTimeoutException("Database connection error"));

            assertThatThrownBy(() -> applicationService.getApplicationByName(name))
            .isInstanceOf(MongoDatabaseIdmException.class)
            .hasMessageContaining("database time out error when querying non-existence of application by name");
        }
                                                                                                                                                                                                                                          
        @Test
        void getApplicationByName_doesNotExistsApplication_IllegalArgumentException(){
            String name = "App";
            Mockito.when(applicationRepository.doesNotExistByName(name)).thenReturn(true);

            assertThatThrownBy(() -> applicationService.getApplicationByName(name))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("does not exist application with this name");
        }
    } 
}

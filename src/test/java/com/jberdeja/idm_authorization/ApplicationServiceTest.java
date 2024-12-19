package com.jberdeja.idm_authorization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.repository.ApplicationRepository;
import com.jberdeja.idm_authorization.service.ApplicationService;
import com.mongodb.MongoException;

public class ApplicationServiceTest extends ContainerConfig{

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
        .hasMessageContaining("this application already exists: " + name);
    }

    @Test
    void createApplication_nameIsEmpty_IllegalArgumentException() {
        String nameAmpty = "";
        ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();
        applicationEntity.setName(nameAmpty);

        assertThatThrownBy(() -> applicationService.createApplication(applicationEntity))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("the application name is not valid");
    }

    @Test
    void createApplication_nameIsNull_IllegalArgumentException() {
        String nameNull = null;
        ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();
        applicationEntity.setName(nameNull);

        assertThatThrownBy(() -> applicationService.createApplication(applicationEntity))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("the application name is not valid");
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
        assertThat(applications.toString()).isEqualTo(applicationExpectations.toString());
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
        .hasMessageContaining("the application name is not valid");
    }

    @Test
    void getApplicationByName_nameIsNull_IllegalArgumentException(){
        String nameNull = null;
        assertThatThrownBy(() -> applicationService.getApplicationByName(nameNull))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("the application name is not valid");
    }
 
    @Nested
    class MockedTests {

        @MockitoBean
        private ApplicationRepository applicationRepository;
        @Autowired
        private ApplicationService applicationService;
    
        @Test
        void createApplication_databaseTransactionError_MongoException() {
 
            Mockito.when(applicationRepository.save(any(ApplicationEntity.class)))
                .thenThrow(new RuntimeException("Error"));

            ApplicationEntity applicationEntity = EntityBuilder.buildApplicationEntity();

            assertThatThrownBy(() -> applicationService.createApplication(applicationEntity))
                .isInstanceOf(MongoException.class)
                .hasMessageContaining("error when saving the application in the database");
        }

        @Test
        void getApplications_databaseSearchError_MongoException(){
            Mockito.when(applicationRepository.findAll()).thenThrow(new RuntimeException("Error"));

            assertThatThrownBy(() -> applicationService.getApplications())
            .isInstanceOf(MongoException.class)
            .hasMessageContaining("error searching for all apps");
        }

        @Test
        void getApplicationByName_databaseSearchError_MongoException(){
            String name = "App";
            Mockito.when(applicationRepository.findByName(any())).thenThrow(new RuntimeException("Error"));
            Mockito.when(applicationRepository.existsByName(any())).thenReturn(true);

            assertThatThrownBy(() -> applicationService.getApplicationByName(name))
            .isInstanceOf(MongoException.class)
            .hasMessageContaining("error when searching for the application by name: " + name);
        }

        @Test
        void getApplicationByName_doesNotExistsApplication_IllegalArgumentException(){
            String name = "App";
            Mockito.when(applicationRepository.existsByName(name)).thenReturn(false);

            assertThatThrownBy(() -> applicationService.getApplicationByName(name))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("does not exist application with this name: " + name);
        }
    }
}

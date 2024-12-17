package com.jberdeja.idm_authorization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.description;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.service.ApplicationService;

public class ApplicationServiceTest extends ContainerConfig{

    @Autowired
    private ApplicationService applicationService;

    @Test
    void createApplication_happyCase_ok() {
        String name = "Application name 1";
        ApplicationEntity applicationEntity = buildApplicationEntityWhitName(name);
        ApplicationEntity savedApplicationEntity = applicationService.createApplication(applicationEntity);

        assertThat(savedApplicationEntity).isNotNull();
        assertThat(savedApplicationEntity.getName()).isEqualTo(name);
    }

    @Test
    void createApplication_alreadyExists_ok() {
        String name = "Application name 2";

        ApplicationEntity applicationEntity = buildApplicationEntityWhitName(name);
        ApplicationEntity savedApplicationEntity = applicationService.createApplication(applicationEntity);

        assertThat(savedApplicationEntity).isNotNull();
        assertThat(savedApplicationEntity.getName()).isEqualTo(name);

/*         assertThatThrownBy(() -> applicationService.createApplication(applicationEntity))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("the application already exists: " + name); */
    }

    private ApplicationEntity buildApplicationEntityWhitName(String name){
        return new ApplicationEntity.Builder()
        .name(name)
        .description("Test Description")
        .build();
    }
}

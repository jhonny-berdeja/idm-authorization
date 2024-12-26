package com.jberdeja.idm_authorization.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.executor.ApplicationRepositoryExecutor;
import com.jberdeja.idm_authorization.utility.Utility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicacionValidator {
    @Autowired
    private ApplicationRepositoryExecutor applicationRepositoryExecutor;

    public void  validateApplicationName(String appName){
        String errorMessage = "error the application name is null or blank";
        Utility.validate(
            appName, 
            Utility::isNullOrBlank, 
            errorMessage
        );
    }

    public void validateIfExistsApplication(String appName){
        String errorMessage = "this application already exists";
        Utility.validate(
            appName, 
            this::existsByName, 
            errorMessage
        );
    }

    public void validateIfNotExistsApplication(String appName){
        String errorMessage = "does not exist application with this name";
        Utility.validate(
            appName, 
            this::doesNotExistByName, 
            errorMessage
        );
    }

    public void validateApplicationEntities(List<ApplicationEntity> applicationEntities){
        String errorMessage = "error, database provided null application list";
        Utility.validate(
            applicationEntities, 
            Utility::isNullObject, 
            errorMessage
        );
    }

    private boolean doesNotExistByName(String applicationName){
        return applicationRepositoryExecutor.doesNotExistByName(applicationName);
    }

    private boolean existsByName(String applicationName){
        return applicationRepositoryExecutor.existsByName(applicationName);
    }
}

package com.jberdeja.idm_authorization.processor;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.executor.ApplicationRepositoryExecutor;
import com.jberdeja.idm_authorization.mapper.ApplicationMapper;
import com.jberdeja.idm_authorization.validator.ApplicacionValidator;

@Component
public class ApplicationProcessor {
    @Autowired
    private ApplicationRepositoryExecutor applicationRepositoryExecutor;
    @Autowired
    private ApplicacionValidator applicacionValidator;
    @Autowired
    private ApplicationMapper applicationMapper;  

    public ApplicationEntity createApplication(ApplicationEntity application){
        validateApplication(application);
        return applicationRepositoryExecutor.save(application);
    }

    public ApplicationEntity getApplicationByName(String applicationName) {
        validateApplicationName(applicationName);
        return applicationRepositoryExecutor.findByName(applicationName);
    }

    public List<String> getApplications(){
        var applicationEntities = applicationRepositoryExecutor.findAll();
        validateApplicationEntities(applicationEntities);
        return applicationMapper.mapToListOfApplicationName(applicationEntities);
    }

    private void validateApplication(ApplicationEntity application){
        String applicationName = application.getName();
        applicacionValidator.validateApplicationName(applicationName);
        applicacionValidator.validateIfExistsApplication(applicationName);
    }

    private void validateApplicationName(String applicationName){
        applicacionValidator.validateApplicationName(applicationName);
        applicacionValidator.validateIfNotExistsApplication(applicationName);
    }

    private void validateApplicationEntities(List<ApplicationEntity> appEntities ){
        applicacionValidator.validateApplicationEntities(appEntities);
    }
}

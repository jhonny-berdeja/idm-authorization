package com.jberdeja.idm_authorization.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.executor.ApplicationRepositoryExecutor;
import com.jberdeja.idm_authorization.mapper.ApplicationMapper;
import com.jberdeja.idm_authorization.validator.ApplicacionValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepositoryExecutor applicationRepositoryExecutor;
    @Autowired
    private ApplicacionValidator applicacionValidator;
    @Autowired
    private ApplicationMapper applicationMapper;  

    public ApplicationEntity createApplication(ApplicationEntity application){
        applicacionValidator.validateApplicationName(application.getName());
        applicacionValidator.validateIfExistsApplication(application.getName());
        return applicationRepositoryExecutor.save(application);
    }

    public ApplicationEntity getApplicationByName(String appName) {
        applicacionValidator.validateApplicationName(appName);
        applicacionValidator.validateIfNotExistsApplication(appName);
        return applicationRepositoryExecutor.findByName(appName);
    }

    public List<String> getApplications(){
        List<ApplicationEntity> applicationEntities = applicationRepositoryExecutor.findAll();
        return applicationMapper.mapToListOfApplicationName(applicationEntities);
    }
}
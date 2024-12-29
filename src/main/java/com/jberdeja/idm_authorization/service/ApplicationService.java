package com.jberdeja.idm_authorization.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.processor.ApplicationProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApplicationService {
    @Autowired
    private ApplicationProcessor applicationProcessor;

    public ApplicationEntity createApplication(ApplicationEntity application){
        log.info("starting application creation");
        var applicationEntity = applicationProcessor.createApplication(application);
        log.info("application creation completed successfully");
        return applicationEntity;
    }

    public ApplicationEntity getApplicationByName(String applicationName) {
        log.info("starting application search by name");
        var applicationEntity =  applicationProcessor.getApplicationByName(
            applicationName
        );
        log.info("application search by name successfully completed");
        return applicationEntity;
    }

    public List<String> getApplications(){
        log.info("starting search for all applications");
        var applications = applicationProcessor.getApplications();
        log.info("search for all applications completed correctly");
        return applications;
    }
}
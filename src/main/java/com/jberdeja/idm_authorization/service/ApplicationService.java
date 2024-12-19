package com.jberdeja.idm_authorization.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.mapper.ApplicationMapper;
import com.jberdeja.idm_authorization.repository.ApplicationRepository;
import com.jberdeja.idm_authorization.validator.ApplicacionValidator;
import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ApplicacionValidator applicacionValidator;
    @Autowired
    private ApplicationMapper applicationMapper;  

    public ApplicationEntity createApplication(ApplicationEntity application){
        applicacionValidator.validateApplicationName(application.getName());
        applicacionValidator.validateIfExistsApplication(application.getName());
        return saveApplication(application);
    }

    public ApplicationEntity getApplicationByName(String appName) {
        applicacionValidator.validateApplicationName(appName);
        applicacionValidator.validateIfNotExistsApplication(appName);
        return findApplicationByName(appName);
    }

    public List<String> getApplications(){
        List<ApplicationEntity> applicationEntities = findApplicationsAll();
        return applicationMapper.mapToListOfApplicationName(applicationEntities);
    }


    private ApplicationEntity saveApplication(ApplicationEntity applicationEntity){
        try{
            return executeSaveApplication(applicationEntity);
        }catch(Exception e){
            log.error("error when saving the application in the database", e);
            throw new MongoException("error when saving the application in the database", e);
        }
    }

    private ApplicationEntity findApplicationByName(String applicationName){
        try{
            return executeFindApplicationByName(applicationName);
        }catch(Exception e){
            log.error("error when searching for the application by name: " + applicationName, e);
            throw new MongoException("error when searching for the application by name: " + applicationName, e);
        }
    }

    private List<ApplicationEntity> findApplicationsAll(){
        try{
            return executeFindApplicationsAll();
        }catch(Exception e){
            log.error("error searching for all apps", e);
            throw new MongoException("error searching for all apps", e);
        }
    }

    @Transactional
    private ApplicationEntity executeSaveApplication(ApplicationEntity applicationEntity){
        return applicationRepository.save(applicationEntity);
    }


    @Transactional
    private ApplicationEntity executeFindApplicationByName(String applicationName){
        return applicationRepository.findByName(applicationName);
    }

    @Transactional
    private List<ApplicationEntity> executeFindApplicationsAll(){
        return applicationRepository.findAll();
    }
}
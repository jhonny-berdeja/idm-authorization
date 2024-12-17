package com.jberdeja.idm_authorization.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Transactional
    public ApplicationEntity createApplication(ApplicationEntity application){
        try{
            validateApplicationName(application.getName());
            validateIfExistsApplication(application.getName());
            return applicationRepository.save(application);
        }catch(Exception e){
            log.error("error when creating the application", e);
            throw e;
        }
    }

    public ApplicationEntity getApplication(String appName) {
        try {
            validateApplicationName(appName);
            validateIfNotExistsApplication(appName);
            return applicationRepository.findByName(appName);
        } catch (Exception e) {
            log.error("Error getting apps with roles", e);
            throw new IllegalArgumentException("Error getting apps with roles", e);
        }
    }

    public List<String> getApplications(){
        try{
            List<ApplicationEntity> applicationEntities = applicationRepository.findAll();
            List<String> listOfApplicationName = mapToListOfApplicationName(applicationEntities);
            return Optional.of(listOfApplicationName).orElseThrow();
        }catch(Exception e){
            log.error("error getting list of application names", e);
            throw new RuntimeException("error getting list of application names", e);
        }
    }
    private List<String> mapToListOfApplicationName(List<ApplicationEntity> applicationEntities ){
        var streamOfApplicationName = mapToStreamOfApplicationName(applicationEntities);
        return streamOfApplicationName.filter(this::isNotValidApplicationName).collect(Collectors.toList());
    }
    private Stream<String> mapToStreamOfApplicationName(List<ApplicationEntity> applicationEntities){
        return applicationEntities.stream().map(ApplicationEntity::getName);
    }

    private void  validateApplicationName(String appName){
        if(isValidApplicationName(appName)){
            log.error("the application name is not valid");
            throw new IllegalArgumentException("the application name is not valid");
        }
    }
    private boolean isNotValidApplicationName(String appName){
        return ! isValidApplicationName(appName);
    }
    private boolean isValidApplicationName(String appName){
        return Objects.isNull(appName) || String.valueOf(appName).isBlank();
    }

    private void validateIfExistsApplication(String applicationName){
        if(applicationRepository.existsByName(applicationName)){
            log.error("the application already exists: " + applicationName);
            throw new IllegalArgumentException("the application already exists: " + applicationName);
        }
    }

    private void validateIfNotExistsApplication(String applicationName){
        if( ! applicationRepository.existsByName(applicationName)){
            log.error("there is no application with this name: " + applicationName);
            throw new IllegalArgumentException("there is no application with this name: " + applicationName);
        }
    }
}
package com.jberdeja.idm_authorization.validator;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jberdeja.idm_authorization.repository.ApplicationRepository;
import com.mongodb.MongoException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicacionValidator {
    @Autowired
    private ApplicationRepository applicationRepository;

    public void  validateApplicationName(String appName){
        if(isValidApplicationName(appName)){
            log.error("the application name is not valid");
            throw new IllegalArgumentException("the application name is not valid");
        }
    }
    public boolean isNotValidApplicationName(String appName){
        return ! isValidApplicationName(appName);
    }
    public boolean isValidApplicationName(String appName){
        return Objects.isNull(appName) || String.valueOf(appName).isBlank();
    }

    public void validateIfExistsApplication(String applicationName){
        if(existsApplicationByName(applicationName)){
            log.error("this application already exists: " + applicationName);
            throw new IllegalArgumentException("this application already exists: " + applicationName);
        }
    }

    public void validateIfNotExistsApplication(String applicationName){
        if( doesNotExistsApplicationByName(applicationName)){
            log.error("does not exist application with this name: " + applicationName);
            throw new IllegalArgumentException("does not exist application with this name: " + applicationName);
        }
    }
    private boolean doesNotExistsApplicationByName(String applicationName){
        return ! existsApplicationByName(applicationName);
    }

    private boolean existsApplicationByName(String applicationName){
        try{
            return applicationRepository.existsByName(applicationName);
        }catch(Exception e){
            log.error("error when querying the existence of the application by name: " + applicationName, e);
            throw new MongoException("error when querying the existence of the application by name: " + applicationName, e);
        }
    }
}

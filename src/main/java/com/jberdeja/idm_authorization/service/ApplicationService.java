package com.jberdeja.idm_authorization.service;

import java.util.List;
import java.util.NoSuchElementException;
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
            validateForApplicationCreation(application);
            return applicationRepository.save(application);
        }catch(Exception e){
            log.error("error when creating the application", e);
            throw new RuntimeException("error when creating the application", e);
        }
    }

    public ApplicationEntity getApplication(String appName) {
        try {
            validateApplicationName(appName);
            var applicationEntitie =  getApplicationOfDatabase(appName);
            return applicationEntitie.orElseThrow(() -> new NoSuchElementException("No application found with the name: " + appName));
        } catch (Exception e) {
            log.error("Error getting apps with roles", e);
            throw new IllegalArgumentException("Error getting apps with roles", e);
        }
    }


    public List<String> getApplications(){
        try{
            var applicationEntities = applicationRepository.findAll();
            var streamOfApplicationName = mapToStreamOfApplicationName(applicationEntities);
            var listOfApplicationName = mapToListOfApplicationName(streamOfApplicationName);
            return Optional.of(listOfApplicationName).orElseThrow();
        }catch(Exception e){
            log.error("error getting list of application names", e);
            throw new RuntimeException("error getting list of application names", e);
        }
    }

    private Stream<String> mapToStreamOfApplicationName(List<ApplicationEntity> applicationEntities){
        return applicationEntities.stream().map(appEntities -> appEntities.getName());
    }

    private List<String> mapToListOfApplicationName(Stream<String> applicationsNames ){
        return applicationsNames.filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void  validateApplicationName(String appName){
        if(isNotValidApplicationName(appName)){
            log.error("the application name is not valid");
            throw new IllegalArgumentException("the application name is not valid");
        }
    }

    private boolean isNotValidApplicationName(String appName){
        return !isValidApplicationName(appName);
    }

    private boolean isValidApplicationName(String appName){
        return Objects.nonNull(appName) && isNotEmptyApplicationName(appName);
    }

    private boolean isNotEmptyApplicationName(String appName){
        return ! appName.trim().isEmpty();
    }

    private void validateForApplicationCreation(ApplicationEntity application){
        validateApplicationName(application.getName());
        validateExistenceOfApplication(application.getName());
    }

    private void validateExistenceOfApplication(String applicationName){
        if(existsApplication(applicationName)){
            log.error("the application already exists");
            throw new IllegalArgumentException("the application already exists");
        }
    }

    private boolean existsApplication(String applicationName){
        var applicationEntities = getApplicationOfDatabase(applicationName);
        return applicationEntities.isPresent();
    }

    private Optional<ApplicationEntity> getApplicationOfDatabase(String applicationName){
        var applicationEntity = applicationRepository.findByName(applicationName);
        return Optional.ofNullable(applicationEntity);
    }
}

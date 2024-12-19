package com.jberdeja.idm_authorization.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.validator.ApplicacionValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationMapper {

        @Autowired
    private ApplicacionValidator applicacionValidator;  

    public List<String> mapToListOfApplicationName(List<ApplicationEntity> applicationEntities ){
        try{
            Stream<String> streamOfApplicationName = mapToStreamOfApplicationName(applicationEntities);
            List<String> listOfApplicationName = streamOfApplicationName.filter(applicacionValidator::isNotValidApplicationName).toList();
            return Optional.of(listOfApplicationName).orElseThrow();
        }catch(Exception e){
            log.error("error when mapping application name stream to application name string list", e);
            throw new MappingException("error when mapping application name stream to application name string list", e);
        }
    }
    private Stream<String> mapToStreamOfApplicationName(List<ApplicationEntity> applicationEntities){
        return applicationEntities.stream().map(ApplicationEntity::getName);
    }
}

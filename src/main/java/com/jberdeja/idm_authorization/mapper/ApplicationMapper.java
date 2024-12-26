package com.jberdeja.idm_authorization.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.entity.ApplicationEntity;
import com.jberdeja.idm_authorization.utility.Utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationMapper {

    public List<String> mapToListOfApplicationName(List<ApplicationEntity> applicationEntities ){
        Stream<String> streamOfApplicationName = mapToStreamOfApplicationName(applicationEntities);
        List<String> listOfApplicationName = mapToStringListOfApplicationNames(streamOfApplicationName);
        return Optional.ofNullable(listOfApplicationName).get();
    }
    
    private Stream<String> mapToStreamOfApplicationName(List<ApplicationEntity> applicationEntities){
        return applicationEntities.stream().map(ApplicationEntity::getName);
    }

    private List<String>  mapToStringListOfApplicationNames(Stream<String> streamOfApplicationName){
        return streamOfApplicationName.filter(Utility::isNotNullOrBlank).toList();
    }
}

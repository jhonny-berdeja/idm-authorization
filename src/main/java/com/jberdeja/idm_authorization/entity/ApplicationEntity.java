package com.jberdeja.idm_authorization.entity;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jberdeja.idm_authorization.entity.creation_application.ApplicationRol;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Document(collection = "applications")
public class ApplicationEntity {
    @Id
    private ObjectId id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private List<ApplicationRol> roles;
}

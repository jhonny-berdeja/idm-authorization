package com.jberdeja.idm_authorization.entity;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jberdeja.idm_authorization.entity.application_creation.ApplicationRol;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Builder
@Data
@Document(collection = "applications")
public class ApplicationEntity {
        @Id
        private ObjectId id;
        @NotNull
        @NotBlank
        private String name;
        @NotNull
        @NotBlank
        private String description;
        @NotNull
        @NotBlank
        private List<ApplicationRol> roles;
}

package com.jberdeja.idm_authorization.entity;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jberdeja.idm_authorization.entity.application_creation.ApplicationRol;

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

        public ApplicationEntity(String name, String description, List<ApplicationRol> roles) {
            this.name = name;
            this.description = description;
            this.roles = roles;
        }

        public static class Builder {
            private String name;
            private String description;
            private List<ApplicationRol> roles;
 
            public Builder name(String name) {
                this.name = name;
                return this;
            }
    
            public Builder description(String description) {
                this.description = description;
                return this;
            }
    
            public Builder roles(List<ApplicationRol> roles) {
                this.roles = roles;
                return this;
            }

            public ApplicationEntity build() {
                return new ApplicationEntity(name, description, roles);
        }
    }
}

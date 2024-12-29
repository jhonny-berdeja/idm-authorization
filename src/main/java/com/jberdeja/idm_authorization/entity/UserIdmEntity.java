package com.jberdeja.idm_authorization.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "users_idm")
@Data
public class UserIdmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String pwd;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles_idm",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleIdmEntity> roles;


        public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String email;
        private String pwd;
        private List<RoleIdmEntity> roles = new ArrayList<>();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder pwd(String pwd) {
            this.pwd = pwd;
            return this;
        }

        public Builder roles(List<RoleIdmEntity> roles) {
            this.roles = roles;
            return this;
        }

        public Builder addRole(RoleIdmEntity role) {
            this.roles.add(role);
            return this;
        }

        public UserIdmEntity build() {
            UserIdmEntity user = new UserIdmEntity();
            user.setId(this.id);
            user.setEmail(this.email);
            user.setPwd(this.pwd);
            user.setRoles(this.roles);
            return user;
        }
    }
}

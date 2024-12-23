package com.jberdeja.idm_authorization.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.entity.RoleIdmEntity;
import com.jberdeja.idm_authorization.executor.RoleIdmRepositoryExecutor;

@Component
public class RoleIdmEntityMapper {
    @Autowired
    private RoleIdmRepositoryExecutor roleIdmRepositoryExecutor;

    public List<RoleIdmEntity>  mapToRoleIdmEntityList(List<String> roles){
        Stream<String>  roleIdmEntity= roles.stream();
        return roleIdmEntity.map(this::findRoleIdmEntityByRoleName).toList();
    }

    public List<SimpleGrantedAuthority> mapToSimpleGrantedAuthorityList(List<RoleIdmEntity> roles){
        return roles.stream().map(this::buildSimpleGrantedAuthority).toList();
    }

    private RoleIdmEntity findRoleIdmEntityByRoleName(String roleName) {
        Optional<RoleIdmEntity>  roleIdmEntityOptional = roleIdmRepositoryExecutor.findByRoleName(roleName);
        validateRoleIdmEntity(roleIdmEntityOptional, roleName);
        return roleIdmEntityOptional.get();
    }

    private void validateRoleIdmEntity(Optional<RoleIdmEntity> roleIdmEntity, String roleName){
        roleIdmEntity.orElseThrow(() -> new IllegalArgumentException("Role idm not found: " + roleName));
    }

    private SimpleGrantedAuthority buildSimpleGrantedAuthority(RoleIdmEntity roleIdmEntity){
        return new SimpleGrantedAuthority(roleIdmEntity.getRoleName());
    }
    
}

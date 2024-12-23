package com.jberdeja.idm_authorization.mapper;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import com.jberdeja.idm_authorization.dto.common.UserIdm;
import com.jberdeja.idm_authorization.entity.RoleIdmEntity;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserMapper {

    @Autowired
    private RoleIdmEntityMapper roleIdmEntityMapper;

    public Optional<User> mapToUser(Optional<UserIdmEntity> userIdmEntityOptional){
        return userIdmEntityOptional.map(this::mapToUser);
    }

    public UserIdmEntity mapToUserIdmEntity(UserIdm userIdm ) {
        List<RoleIdmEntity> roles = mapToRoleIdmEntityList(userIdm.getRoles());
        return buildUserIdmEntity(userIdm, roles);
    }

    private User mapToUser(UserIdmEntity userIdmEntity){
        List<RoleIdmEntity> roles = userIdmEntity.getRoles();
        List<SimpleGrantedAuthority> authorities = mapToSimpleGrantedAuthorityList(roles);
        return buildUser(userIdmEntity, authorities);
    }
    
    private User buildUser(UserIdmEntity userIdmEntity, List<SimpleGrantedAuthority> authorities){
        return new User(userIdmEntity.getEmail(), userIdmEntity.getPwd(), authorities);
    }

    private List<SimpleGrantedAuthority> mapToSimpleGrantedAuthorityList(List<RoleIdmEntity> roles){
        return roleIdmEntityMapper.mapToSimpleGrantedAuthorityList(roles);
    }

    private List<RoleIdmEntity> mapToRoleIdmEntityList(List<String> roles){
        return roleIdmEntityMapper.mapToRoleIdmEntityList(roles);
    }

    private UserIdmEntity buildUserIdmEntity(UserIdm userIdm, List<RoleIdmEntity> roles){
        UserIdmEntity user = new UserIdmEntity();
        user.setEmail(userIdm.getEmail());
        user.setPwd(userIdm.getPassword());
        user.setRoles(roles);
        return user;
    }
}

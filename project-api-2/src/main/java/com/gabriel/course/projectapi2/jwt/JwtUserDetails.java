package com.gabriel.course.projectapi2.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtUserDetails extends User {

    com.gabriel.course.projectapi2.model.User userEntity;
    public JwtUserDetails(com.gabriel.course.projectapi2.model.User userEntity) {
        super(userEntity.getUsername(), userEntity.getPassword(), AuthorityUtils.createAuthorityList(userEntity.getRole().name()));
        this.userEntity = userEntity;
    }

    public Long getId() {
        return userEntity.getId();
    }

    public String getRole() {
        return  userEntity.getRole().name();
    }
}

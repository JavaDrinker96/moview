package com.example.moview.mapper;

import com.example.moview.model.Role;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Named("entityToSimpleAuthority")
    default SimpleGrantedAuthority entityToSimpleAuthority(Role role) {
        return new SimpleGrantedAuthority(role.getName());
    }

    @IterableMapping(qualifiedByName = "entityToSimpleAuthority")
    Collection<SimpleGrantedAuthority> entitySetToAuthoritiesList(Set<Role> roles);
}
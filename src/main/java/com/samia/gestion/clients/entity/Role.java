package com.samia.gestion.clients.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.samia.gestion.clients.entity.RolePermission.*;

public enum Role {
    USER(
            Set.of(
                    USER_CREATE,
                    USER_UPDATE,
                    USER_READ,
                    USER_DELETE
            )
    ),
    ADMIN(
            Set.of(
                  ADMIN_CREATE,
                  ADMIN_UPDATE,
                  ADMIN_READ,
                  ADMIN_DELETE
            )
    );

    Role(){}

    Set<RolePermission> permissions;

    Role(Set<RolePermission> permissions) {
        this.permissions = permissions;
    }

    public Set<RolePermission> getPermissions() {
        return permissions;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<SimpleGrantedAuthority> grantedAuthorityList = this.getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.name())
        ).collect(Collectors.toList());
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorityList;
    }
    }

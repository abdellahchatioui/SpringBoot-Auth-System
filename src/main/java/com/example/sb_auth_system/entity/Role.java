package com.example.sb_auth_system.entity;

import java.util.Set;

public enum Role {
    USER(Set.of(
            Permission.READ_USER
    )),


    ADMIN(Set.of(
            Permission.READ_USER,
            Permission.CREATE_USER,
            Permission.DELETE_USER
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
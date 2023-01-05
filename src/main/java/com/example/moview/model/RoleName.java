package com.example.moview.model;

import lombok.Getter;

@Getter
public enum RoleName {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    SUPER_ADMIN("SUPER_ADMIN");

    private String value;

    RoleName(String value) {
        this.value = value;
    }

}
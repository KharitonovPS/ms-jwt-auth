package org.kharitonov.ms_jwt_auth.model;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_ADMIN("ROLE_ADMIN"), ROLE_USER("ROLE_USER");
    private final String stringValue;

    UserRole(String stringValue) {
        this.stringValue = stringValue;
    }
}

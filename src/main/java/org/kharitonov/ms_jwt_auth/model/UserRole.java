package org.kharitonov.ms_jwt_auth.model;

public enum UserRole {
    ROLE_ADMIN("ADMIN"), ROLE_USER("USER"), ROLE_UNAUTHORIZED("UNAUTHORIZED");

    private String stringValue;

    UserRole(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static UserRole fromString(String text) {
        for (UserRole myEnum : UserRole.values()) {
            if (myEnum.stringValue.equalsIgnoreCase(text)) {
                return myEnum;
            }
        }
        return null;
    }
}

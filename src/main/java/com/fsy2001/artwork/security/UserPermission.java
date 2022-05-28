package com.fsy2001.artwork.security;

public enum UserPermission {
    USER_ALL("user:all");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

package com.softserve.itacademy.security;

public enum Permissions {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    TASK_READ("task:read"),
    TASK_WRITE("task:write"),
    TODO_READ("todo:read"),
    TODO_WRITE("todo:write"),
    ROLE_READ("role:read"),
    ROLE_WRITE("role:write"),
    ALL_READ("all:read");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

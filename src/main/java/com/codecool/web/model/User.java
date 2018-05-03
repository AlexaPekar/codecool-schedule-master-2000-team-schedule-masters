package com.codecool.web.model;

public final class User extends AbstractModel {

    private final String name;
    private final String password;
    private final String role;

    public User(int id, String name, String password, String role) {
        super(id);
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

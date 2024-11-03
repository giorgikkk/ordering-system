package com.example.orderservice.model.dto;

import java.util.List;

public class UserResponse {
    private String username;
    private List<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }
}

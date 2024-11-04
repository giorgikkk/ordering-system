package com.example.authorizationservice.decorator;

import com.example.authorizationservice.model.Role;
import org.springframework.security.core.GrantedAuthority;

public class SecurityRole implements GrantedAuthority {
    private final Role role;

    public SecurityRole(final Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getName().name();
    }
}

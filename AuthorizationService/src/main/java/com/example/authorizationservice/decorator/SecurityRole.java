package com.example.authorizationservice.decorator;

import com.example.authorizationservice.model.Role;
import org.springframework.security.core.GrantedAuthority;

public class SecurityRole implements GrantedAuthority {
    private Role role;

    public SecurityRole(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getName().name();
    }
}

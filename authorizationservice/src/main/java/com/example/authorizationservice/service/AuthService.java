package com.example.authorizationservice.service;

import com.example.authorizationservice.model.Role;
import com.example.authorizationservice.model.enums.RoleType;
import com.example.authorizationservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final RoleRepository roleRepository;

    @Autowired
    public AuthService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(final RoleType role) {
        return roleRepository.findByRole(role)
                .orElseThrow(() -> new RuntimeException("Role not found: " + role));
    }
}

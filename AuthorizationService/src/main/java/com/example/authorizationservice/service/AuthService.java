package com.example.authorizationservice.service;

import com.example.authorizationservice.model.Role;
import com.example.authorizationservice.model.enums.RoleType;
import com.example.authorizationservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private RoleRepository roleRepository;

    @Autowired
    public AuthService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(RoleType name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }
}

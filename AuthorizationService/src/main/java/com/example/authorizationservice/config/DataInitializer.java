package com.example.authorizationservice.config;

import com.example.authorizationservice.model.Role;
import com.example.authorizationservice.model.enums.RoleType;
import com.example.authorizationservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Autowired
    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName(RoleType.ADMINISTRATOR).isEmpty()) {
            roleRepository.save(new Role(RoleType.ADMINISTRATOR));
        }
        if (roleRepository.findByName(RoleType.CLIENT).isEmpty()) {
            roleRepository.save(new Role(RoleType.CLIENT));
        }
        if (roleRepository.findByName(RoleType.SELLER).isEmpty()) {
            roleRepository.save(new Role(RoleType.SELLER));
        }
    }
}
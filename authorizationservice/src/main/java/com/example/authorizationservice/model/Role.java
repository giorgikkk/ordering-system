package com.example.authorizationservice.model;

import com.example.authorizationservice.model.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", unique = true, nullable = false)
    private RoleType role;

    public Role() {
    }

    public Role(final RoleType role) {
        this.role = role;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(final RoleType role) {
        this.role = role;
    }
}

package com.example.authorizationservice.model;

import com.example.authorizationservice.model.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleType name;

    public Role() {
    }

    public Role(RoleType name) {
        this.name = name;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }
}

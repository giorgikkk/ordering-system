package com.example.orderservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {
    @JsonProperty("name")
    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(final String authority) {
        this.authority = authority;
    }
}

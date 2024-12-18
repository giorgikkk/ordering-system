package com.example.orderservice.model.dto;

import java.util.List;

public class OrderRequest {
    private String clientUsername;
    private String clientPhoneNumber;

    private List<Long> productIds;
    private List<Integer> quantities;

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(final String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(final String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(final List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(final List<Integer> quantities) {
        this.quantities = quantities;
    }
}

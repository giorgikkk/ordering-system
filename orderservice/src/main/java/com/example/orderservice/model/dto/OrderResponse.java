package com.example.orderservice.model.dto;

public class OrderResponse {
    private Long orderId;
    private String message;

    public OrderResponse(final Long orderId, final String message) {
        this.orderId = orderId;
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}

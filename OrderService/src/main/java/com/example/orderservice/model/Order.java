package com.example.orderservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_gen")
    @SequenceGenerator(name = "order_id_gen", sequenceName = "order_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Client username cannot be empty.")
    @Column(name = "client_username", nullable = false)
    private String clientUsername;

    @ElementCollection
    @NotEmpty(message = "Product IDs cannot be empty.")
    @Column(name = "product_ids", nullable = false)
    private List<String> productIds;

    @ElementCollection
    @NotEmpty(message = "Quantities cannot be empty.")
    @Column(name = "quantities", nullable = false)
    private List<Integer> quantities;

    @Column(name = "status", nullable = false)
    private String status;

    public Order() {
    }

    public Order(final Long id,
                 final String clientUsername,
                 final List<String> productIds,
                 final List<Integer> quantities,
                 final String status) {
        this.id = id;
        this.clientUsername = clientUsername;
        this.productIds = productIds;
        this.quantities = quantities;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(final String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(final List<String> productIds) {
        this.productIds = productIds;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(final List<Integer> quantities) {
        this.quantities = quantities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}

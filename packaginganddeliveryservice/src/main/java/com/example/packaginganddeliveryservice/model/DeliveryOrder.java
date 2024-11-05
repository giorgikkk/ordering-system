package com.example.packaginganddeliveryservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder {
    @Id
    @Column(name = "delivery_order_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_order_id_gen")
    @SequenceGenerator(name = "delivery_order_id_gen", sequenceName = "delivery_order_id_seq", allocationSize = 1)
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Client username cannot be empty.")
    @Column(name = "client_username", nullable = false)
    @JsonProperty("client_username")
    private String clientUsername;

    @ElementCollection
    @NotEmpty(message = "Product IDs cannot be empty.")
    @Column(name = "product_ids", nullable = false)
    @JsonProperty("product_ids")
    private List<String> productIds;

    @ElementCollection
    @NotEmpty(message = "Quantities cannot be empty.")
    @Column(name = "quantities", nullable = false)
    @JsonProperty("quantities")
    private List<Integer> quantities;

    @Past
    @NotNull
    @Column(name = "delivery_order_date", nullable = false)
    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_order_status_type")
    private DeliveryOrderStatus orderStatus;

    public DeliveryOrder(@JsonProperty("id") final Long id,
                         @JsonProperty("client_username") final String clientUsername,
                         @JsonProperty("product_ids") final List<String> productIds,
                         @JsonProperty("quantities") final List<Integer> quantities,
                         @JsonProperty("order_date") final LocalDateTime orderDate,
                         @JsonProperty("order_status") final DeliveryOrderStatus orderStatus) {
        this.id = id;
        this.clientUsername = clientUsername;
        this.productIds = productIds;
        this.quantities = quantities;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

    public DeliveryOrder() {
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

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public DeliveryOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final DeliveryOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

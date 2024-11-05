package com.example.orderservice.model;

import com.example.orderservice.model.enums.OrderStatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
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

    @NotBlank
    @Column(name = "client_phone_number", unique = true, nullable = false)
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number format")
    private String clientPhoneNumber;

    @NotEmpty
    @ElementCollection
    @NotEmpty(message = "Seller's emails cannot be empty.")
    @Column(name = "sellers_emails", unique = true, nullable = false)
    private List<String> sellersEmails;

    @ElementCollection
    @NotEmpty(message = "Product IDs cannot be empty.")
    @Column(name = "product_ids", nullable = false)
    private List<String> productIds;

    @ElementCollection
    @NotEmpty(message = "Quantities cannot be empty.")
    @Column(name = "quantities", nullable = false)
    private List<Integer> quantities;

    @Past
    @NotNull
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", nullable = false)
    private OrderStatusType status;

    public Order() {
    }

    public Order(final Long id,
                 final String clientUsername,
                 final String clientPhoneNumber,
                 final List<String> sellersEmails,
                 final List<String> productIds,
                 final List<Integer> quantities,
                 final LocalDateTime orderDate,
                 final OrderStatusType status) {
        this.id = id;
        this.clientUsername = clientUsername;
        this.clientPhoneNumber = clientPhoneNumber;
        this.sellersEmails = sellersEmails;
        this.productIds = productIds;
        this.quantities = quantities;
        this.orderDate = orderDate;
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

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(final String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public List<String> getSellersEmails() {
        return sellersEmails;
    }

    public void setSellersEmails(final List<String> sellersEmails) {
        this.sellersEmails = sellersEmails;
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

    public OrderStatusType getStatus() {
        return status;
    }

    public void setStatus(final OrderStatusType status) {
        this.status = status;
    }
}

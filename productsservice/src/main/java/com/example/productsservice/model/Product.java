package com.example.productsservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_gen")
    @SequenceGenerator(name = "product_id_gen", sequenceName = "product_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Product name cannot be empty.")
    @Column(name = "name", nullable = false)
    private String name;

    @Min(value = 0, message = "Price must be a positive value.")
    @Column(name = "price", nullable = false)
    private Double price;

    @Min(value = 0, message = "Quantity must be a non-negative value.")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ElementCollection
    @NotEmpty(message = "Seller IDs cannot be empty.")
    @Column(name = "seller_ids", nullable = false)
    private Set<Long> sellerIds = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<Long> getSellerIds() {
        return sellerIds;
    }

    public void setSellerIds(final Set<Long> sellerIds) {
        this.sellerIds = sellerIds;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

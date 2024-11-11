package com.example.productsservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "seller_product_stock")
public class SellerProductStock {
    @Id
    @Column(name = "seller_product_stock_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seller_product_stock_id_gen")
    @SequenceGenerator(name = "seller_product_stock_id_gen", sequenceName = "seller_product_stock_id_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "Seller ID cannot be null.")
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @NotNull(message = "Product cannot be null.")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 0, message = "Quantity must be a non-negative value.")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(final Long sellerId) {
        this.sellerId = sellerId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

package com.example.productsservice.model;

import com.example.authorizationservice.model.User;
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

    @NotNull(message = "Seller cannot be null.")
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @NotNull(message = "Product cannot be null.")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(value = 0, message = "Quantity must be a non-negative value.")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
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

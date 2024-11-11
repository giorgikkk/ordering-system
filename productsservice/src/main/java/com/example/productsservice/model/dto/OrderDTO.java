package com.example.productsservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderDTO {
    @NotEmpty(message = "Product IDs cannot be empty.")
    @JsonProperty("product_ids")
    private List<Long> productIds;

    @NotEmpty(message = "Quantities cannot be empty.")
    @JsonProperty("quantities")
    private List<Integer> quantities;

    public OrderDTO(@JsonProperty("product_ids") List<Long> productIds, @JsonProperty("quantities") List<Integer> quantities) {
        this.productIds = productIds;
        this.quantities = quantities;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Integer> quantities) {
        this.quantities = quantities;
    }
}

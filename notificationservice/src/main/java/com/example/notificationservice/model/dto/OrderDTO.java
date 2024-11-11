package com.example.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderDTO {
    @NotEmpty(message = "Order ID cannot be empty.")
    @JsonProperty("id")
    private String id;

    @NotEmpty(message = "Client username cannot be empty.")
    @JsonProperty("client_username")
    private String clientUsername;

    @NotEmpty(message = "Client phone number cannot be empty.")
    @JsonProperty("client_phone_number")
    private String clientPhoneNumber;

    @NotEmpty(message = "Seller emails cannot be empty.")
    @JsonProperty("seller_emails")
    private List<String> sellersEmails;

    @NotEmpty(message = "Product IDs cannot be empty.")
    @JsonProperty("product_ids")
    private List<Long> productIds;

    @NotEmpty(message = "Quantities cannot be empty.")
    @JsonProperty("quantities")
    private List<Integer> quantities;

    public OrderDTO(@JsonProperty("id") String id,
                    @JsonProperty("client_username") String clientUsername,
                    @JsonProperty("client_phone_number") String clientPhoneNumber,
                    @JsonProperty("seller_emails") List<String> sellerEmails,
                    @JsonProperty("product_ids") List<Long> productIds,
                    @JsonProperty("quantities") List<Integer> quantities) {
        this.id = id;
        this.clientUsername = clientUsername;
        this.clientPhoneNumber = clientPhoneNumber;
        this.sellersEmails = sellerEmails;
        this.productIds = productIds;
        this.quantities = quantities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public List<String> getSellersEmails() {
        return sellersEmails;
    }

    public void setSellersEmails(final List<String> sellersEmails) {
        this.sellersEmails = sellersEmails;
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

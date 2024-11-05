package com.example.packaginganddeliveryservice.model;

import com.example.packaginganddeliveryservice.model.enums.DeliveryOrderStatusType;
import jakarta.persistence.*;

@Entity
@Table(name = "delivery_order_status")
public class DeliveryOrderStatus {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_order_status_type", unique = true, nullable = false)
    private DeliveryOrderStatusType status;

    public DeliveryOrderStatus() {
    }

    public DeliveryOrderStatus(final DeliveryOrderStatusType status) {
        this.status = status;
    }

    public DeliveryOrderStatusType getStatus() {
        return status;
    }

    public void setName(final DeliveryOrderStatusType status) {
        this.status = status;
    }
}

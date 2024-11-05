package com.example.packaginganddeliveryservice.config;

import com.example.packaginganddeliveryservice.model.DeliveryOrderStatus;
import com.example.packaginganddeliveryservice.model.enums.DeliveryOrderStatusType;
import com.example.packaginganddeliveryservice.repository.DeliveryOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final DeliveryOrderStatusRepository orderStatusRepository;

    @Autowired
    public DataInitializer(final DeliveryOrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    @Override
    public void run(final String... args) {
        if (orderStatusRepository.findByStatus(DeliveryOrderStatusType.PENDING).isEmpty()) {
            orderStatusRepository.save(new DeliveryOrderStatus(DeliveryOrderStatusType.PENDING));
        }
        if (orderStatusRepository.findByStatus(DeliveryOrderStatusType.DELIVERED).isEmpty()) {
            orderStatusRepository.save(new DeliveryOrderStatus(DeliveryOrderStatusType.DELIVERED));
        }
        if (orderStatusRepository.findByStatus(DeliveryOrderStatusType.CANCELLED).isEmpty()) {
            orderStatusRepository.save(new DeliveryOrderStatus(DeliveryOrderStatusType.CANCELLED));
        }
    }
}
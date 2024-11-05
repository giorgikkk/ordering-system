package com.example.packaginganddeliveryservice.service;

import com.example.packaginganddeliveryservice.model.DeliveryOrder;
import com.example.packaginganddeliveryservice.model.DeliveryOrderStatus;
import com.example.packaginganddeliveryservice.repository.DeliveryOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryOrderService {
    private final DeliveryOrderRepository orderRepository;

    @Autowired
    public DeliveryOrderService(final DeliveryOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrder(final DeliveryOrder deliveryOrder) {
        orderRepository.save(deliveryOrder);
    }

    public List<DeliveryOrder> getOrdersByStatus(final DeliveryOrderStatus status) {
        return orderRepository.findByOrderStatus(status);
    }

    public List<DeliveryOrder> getOrdersByDateRange(final LocalDateTime startDate, final LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
}

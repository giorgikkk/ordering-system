package com.example.packaginganddeliveryservice.repository;

import com.example.packaginganddeliveryservice.model.DeliveryOrder;
import com.example.packaginganddeliveryservice.model.DeliveryOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {
    List<DeliveryOrder> findByOrderStatus(final DeliveryOrderStatus status);
    List<DeliveryOrder> findByOrderDateBetween(final LocalDateTime startDate, final LocalDateTime endDate);
}

package com.example.packaginganddeliveryservice.repository;

import com.example.packaginganddeliveryservice.model.DeliveryOrderStatus;
import com.example.packaginganddeliveryservice.model.enums.DeliveryOrderStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryOrderStatusRepository extends JpaRepository<DeliveryOrderStatus, Long> {
    Optional<DeliveryOrderStatus> findByStatus(DeliveryOrderStatusType status);

}

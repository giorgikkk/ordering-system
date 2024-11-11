package com.example.productsservice.repository;

import com.example.productsservice.model.SellerProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerProductStockRepository extends JpaRepository<SellerProductStock, Long> {
    @Query("SELECT sps FROM SellerProductStock sps WHERE sps.product.id = :productId AND sps.sellerId = :sellerId")
    Optional<SellerProductStock> findByProductAndSeller(@Param("productId") Long productId, @Param("sellerId") Long sellerId);
}

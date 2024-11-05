package com.example.productsservice.service;

import com.example.productsservice.model.SellerProductStock;
import com.example.productsservice.repository.SellerProductStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerProductStockService {
    private final SellerProductStockRepository sellerProductStockRepository;

    @Autowired
    public SellerProductStockService(final SellerProductStockRepository sellerProductStockRepository) {
        this.sellerProductStockRepository = sellerProductStockRepository;
    }

    public Optional<SellerProductStock> getStockbySellerAndProduct(final Long productId, final Long sellerId) {
        return sellerProductStockRepository.findByProductAndSeller(productId, sellerId);
    }
}

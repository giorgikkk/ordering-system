package com.example.productsservice.listener;

import com.example.productsservice.config.RabbitMQConfig;
import com.example.productsservice.model.Product;
import com.example.productsservice.model.dto.OrderDTO;
import com.example.productsservice.repository.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderListener {
    private final ProductRepository productRepository;

    @Autowired
    public OrderListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void receiveOrder(OrderDTO order) {
        List<String> productIds = order.getProductIds();
        List<Integer> quantities = order.getQuantities();

        for (int i = 0; i < productIds.size(); i++) {
            String productId = productIds.get(i);
            int quantityToReduce = quantities.get(i);
            Long id = Long.parseLong(productId);

            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            if (product.getQuantity() >= quantityToReduce) {
                product.setQuantity(product.getQuantity() - quantityToReduce);
                productRepository.save(product);
            } else {
                throw new RuntimeException("Insufficient quantity for product: " + productId);
            }
        }
    }
}

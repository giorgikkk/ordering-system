package com.example.orderservice.service;

import com.example.authorizationservice.service.UserService;
import com.example.orderservice.client.AuthServiceClient;
import com.example.orderservice.config.RabbitMQConfig;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.dto.OrderRequest;
import com.example.orderservice.model.dto.OrderResponse;
import com.example.orderservice.model.dto.UserResponse;
import com.example.orderservice.model.enums.OrderStatusType;
import com.example.orderservice.repository.OrderRepository;
import com.example.productsservice.model.Product;
import com.example.productsservice.model.SellerProductStock;
import com.example.productsservice.service.ProductService;
import com.example.productsservice.service.SellerProductStockService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final AuthServiceClient authServiceClient;
    private final ProductService productService;
    private final UserService userService;
    private final SellerProductStockService sellerProductStockService;

    @Autowired
    public OrderService(final OrderRepository orderRepository, final RabbitTemplate rabbitTemplate, final AuthServiceClient authServiceClient, final ProductService productService, final UserService userService, final SellerProductStockService sellerProductStockService) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.authServiceClient = authServiceClient;
        this.productService = productService;
        this.userService = userService;
        this.sellerProductStockService = sellerProductStockService;
    }

    @Transactional
    public OrderResponse placeOrder(final OrderRequest orderRequest, final String token) {
        verifyClientRole(orderRequest, token);

        List<Long> productIds = orderRequest.getProductIds();
        List<Integer> quantities = orderRequest.getQuantities();

        List<Long> sellerIds = validateProductsAndFindSellers(productIds, quantities);

        Order order = createOrder(orderRequest, sellerIds);

        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", order);

        return new OrderResponse(order.getId(), "Order placed successfully");
    }


    private void verifyClientRole(final OrderRequest orderRequest, final String token) {
        final UserResponse userResponse = authServiceClient.getUserDetails(orderRequest.getClientUsername(), token);

        if (userResponse == null || userResponse.getRoles() == null || userResponse.getRoles().isEmpty()) {
            throw new RuntimeException("User is not authorized to place an order");
        }

        final boolean hasClientRole = userResponse.getRoles().stream()
                .anyMatch(role -> "CLIENT".equals(role.getAuthority()));

        if (!hasClientRole) {
            throw new RuntimeException("User does not have CLIENT role");
        }
    }


    private List<Long> validateProductsAndFindSellers(final List<Long> productIds, final List<Integer> quantities) {
        List<Long> sellerIds = new ArrayList<>();

        for (int i = 0; i < productIds.size(); i++) {
            long requestedProductId = productIds.get(i);
            int requestedProductQuantity = quantities.get(i);

            Product product = productService.getProductById(requestedProductId);
            if (product == null) {
                sellerIds.add(null);
                continue;
            }

            Set<Long> productSellerIds = product.getSellerIds();
            boolean sellerFound = false;

            for (final Long sellerId : productSellerIds) {
                if (userService.isSeller(sellerId)) {
                    Optional<SellerProductStock> stockOptional = sellerProductStockService.getStockbySellerAndProduct(requestedProductId, sellerId);
                    if (stockOptional.isPresent()) {
                        SellerProductStock stock = stockOptional.get();
                        if (stock.getQuantity() >= requestedProductQuantity) {
                            sellerIds.add(sellerId);
                            sellerFound = true;
                            break;
                        }
                    }
                }
            }

            if (!sellerFound) {
                throw new RuntimeException("No valid seller with sufficient stock for product: " + product.getName() + ", requested quantity: " + requestedProductQuantity);
            }
        }
        return sellerIds;
    }

    private Order createOrder(final OrderRequest orderRequest, final List<Long> sellerIds) {
        List<String> sellersEmails = sellerIds.stream().map(userService::getEmail).toList();

        final Order order = new Order();
        order.setClientUsername(orderRequest.getClientUsername());
        order.setClientPhoneNumber(orderRequest.getClientPhoneNumber());
        order.setSellersEmails(sellersEmails);
        order.setProductIds(orderRequest.getProductIds());
        order.setQuantities(orderRequest.getQuantities());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatusType.PENDING);

        orderRepository.save(order);

        return order;
    }
}

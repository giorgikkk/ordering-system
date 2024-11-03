package com.example.orderservice.service;

import com.example.orderservice.client.AuthServiceClient;
import com.example.orderservice.config.RabbitMQConfig;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.dto.OrderRequest;
import com.example.orderservice.model.dto.OrderResponse;
import com.example.orderservice.model.dto.UserResponse;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final AuthServiceClient authServiceClient;

    @Autowired
    public OrderService(final OrderRepository orderRepository, final RabbitTemplate rabbitTemplate, final AuthServiceClient authServiceClient) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.authServiceClient = authServiceClient;
    }

    @Transactional
    public OrderResponse placeOrder(final OrderRequest orderRequest, final String token) {
        final UserResponse userResponse = authServiceClient.getUserDetails(orderRequest.getClientUsername(), token);

        if (userResponse == null || userResponse.getRoles() == null || userResponse.getRoles().isEmpty()) {
            throw new RuntimeException("User is not authorized to place an order");
        }

        final boolean hasClientRole = userResponse.getRoles().stream()
                .anyMatch(role -> "CLIENT".equals(role.getAuthority()));

        if (!hasClientRole) {
            throw new RuntimeException("User does not have CLIENT role");
        }

        final Order order = new Order();
        order.setClientUsername(orderRequest.getClientUsername());
        order.setProductIds(orderRequest.getProductIds());
        order.setQuantities(orderRequest.getQuantities());
        order.setStatus("Pending");
        orderRepository.save(order);

        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, order);
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, order);

        return new OrderResponse(order.getId(), "Order placed successfully");
    }
}

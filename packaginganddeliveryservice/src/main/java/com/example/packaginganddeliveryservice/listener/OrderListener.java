package com.example.packaginganddeliveryservice.listener;

import com.example.packaginganddeliveryservice.config.RabbitMQConfig;
import com.example.packaginganddeliveryservice.model.DeliveryOrder;
import com.example.packaginganddeliveryservice.service.DeliveryOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    private final DeliveryOrderService orderService;

    @Autowired
    public OrderListener(DeliveryOrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void receiveOrder(final DeliveryOrder order) {
        orderService.saveOrder(order);
    }
}

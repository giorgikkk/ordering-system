package com.example.orderservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String ORDER_QUEUE_1 = "orderQueue1";
    public static final String ORDER_QUEUE_2 = "orderQueue2";
    public static final String ORDER_QUEUE_3 = "orderQueue3";
    public static final String FANOUT_EXCHANGE = "orderFanoutExchange";

    @Bean
    public Queue orderQueue1() {
        return new Queue(ORDER_QUEUE_1, true);
    }

    @Bean
    public Queue orderQueue2() {
        return new Queue(ORDER_QUEUE_2, true);
    }

    @Bean
    public Queue orderQueue3() {
        return new Queue(ORDER_QUEUE_3, true);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding bindingQueue1(FanoutExchange fanoutExchange, Queue orderQueue1) {
        return BindingBuilder.bind(orderQueue1).to(fanoutExchange);
    }

    @Bean
    public Binding bindingQueue2(FanoutExchange fanoutExchange, Queue orderQueue2) {
        return BindingBuilder.bind(orderQueue2).to(fanoutExchange);
    }

    @Bean
    public Binding bindingQueue3(FanoutExchange fanoutExchange, Queue orderQueue3) {
        return BindingBuilder.bind(orderQueue3).to(fanoutExchange);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}

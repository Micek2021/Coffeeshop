package com.coffeeshop.order.messaging;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange orderPlacedExchange(){
        return new DirectExchange("order.placed.exchange");
    }

    @Bean
    public DirectExchange orderStatusExchange(){
        return new DirectExchange("order.status.exchange");
    }

    @Bean
    public Queue inventoryQueue(){
        return new Queue("inventory.queue");
    }

    @Bean
    public Queue notificationQueue(){
        return new Queue("notification.queue");
    }

    @Bean
    public Queue inventoryResponseQueue(){
        return new Queue("inventory.response.queue");
    }

    @Bean
    public Binding inventoryOrderPlaceBinding(){
        return BindingBuilder
                .bind(inventoryQueue())
                .to(orderPlacedExchange())
                .with("order.placed");
    }

    @Bean
    public Binding notificationOrderStatusBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(orderStatusExchange())
                .with("order.status");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}

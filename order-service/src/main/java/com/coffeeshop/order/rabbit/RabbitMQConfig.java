package com.coffeeshop.order.rabbit;

import com.coffeeshop.rabbitmq.config.SharedRabbitMQConfig;
import com.coffeeshop.rabbitmq.config.inventory.InventoryMessagingConstants;
import com.coffeeshop.rabbitmq.config.notification.NotificationMessagingConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import(SharedRabbitMQConfig.class)
public class RabbitMQConfig {

    @Bean
    public DirectExchange orderPlacedExchange(){
        return new DirectExchange(InventoryMessagingConstants.ORDER_PLACED_EXCHANGE);
    }

    @Bean
    public Binding inventoryOrderPlaceBinding(){
        return BindingBuilder
                .bind(new Queue(InventoryMessagingConstants.INVENTORY_QUEUE, true))
                .to(orderPlacedExchange())
                .with(InventoryMessagingConstants.ROUTING_KEY_PLACED);
    }

    @Bean
    public Queue inventoryResponseQueue(){
        return new Queue(InventoryMessagingConstants.INVENTORY_RESPONSE_QUEUE);
    }

    @Bean
    public DirectExchange orderStatusExchange(){
        return new DirectExchange(NotificationMessagingConstants.ORDER_STATUS_EXCHANGE);
    }

    @Bean
    public Binding notificationOrderStatusBinding() {
        return BindingBuilder
                .bind(new Queue(NotificationMessagingConstants.NOTIFICATION_QUEUE, true))
                .to(orderStatusExchange())
                .with(NotificationMessagingConstants.ROUTING_KEY_STATUS);
    }
}

package com.coffeeshop.inventory.rabbit;

import com.coffeeshop.rabbitmq.config.SharedRabbitMQConfig;
import com.coffeeshop.rabbitmq.config.inventory.InventoryMessagingConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SharedRabbitMQConfig.class)
public class RabbitMQConfig {

    @Bean
    public Queue inventoryQueue() {
        return new Queue(InventoryMessagingConstants.INVENTORY_QUEUE, true);
    }
}

package com.coffeeshop.notification.rabbit;

import com.coffeeshop.rabbitmq.config.SharedRabbitMQConfig;
import com.coffeeshop.rabbitmq.config.notification.NotificationMessagingConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SharedRabbitMQConfig.class)
public class RabbitMQConfig {

    @Bean
    public Queue orderStatusQueue() {
        return new Queue(NotificationMessagingConstants.NOTIFICATION_QUEUE, true);
    }
}

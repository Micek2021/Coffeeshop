package com.coffeeshop.notification.rabbit;

import com.coffeeshop.rabbitmq.OrderStatusChangedEvent;
import com.coffeeshop.rabbitmq.config.notification.NotificationMessagingConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = NotificationMessagingConstants.NOTIFICATION_QUEUE)
    public void handleOrderStatusChanger(OrderStatusChangedEvent event) {
        log.info("Received status change for order {}: {}", event.getOrderId(), event.getStatus());

        messagingTemplate.convertAndSend(
                "/topic/orders/" + event.getCustomerName(),
                event
        );
    }
}

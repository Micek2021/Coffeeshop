package com.coffeeshop.order.rabbit;

import com.coffeeshop.rabbitmq.OrderPlacedEvent;
import com.coffeeshop.rabbitmq.OrderStatusChangedEvent;
import com.coffeeshop.order.model.Order;
import com.coffeeshop.rabbitmq.config.inventory.InventoryMessagingConstants;
import com.coffeeshop.rabbitmq.config.notification.NotificationMessagingConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishOrderPlaced(Order order){
        OrderPlacedEvent event = new OrderPlacedEvent(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getCustomerName()
        );

        rabbitTemplate.convertAndSend(InventoryMessagingConstants.ORDER_PLACED_EXCHANGE, InventoryMessagingConstants.ROUTING_KEY_PLACED, event);
        log.info("Published OrderPlacedEvent for order: {}", order.getId());
    }

    public void publishOrderStatusChanged(Order order){
        OrderStatusChangedEvent event = new OrderStatusChangedEvent(
                order.getId(),
                order.getStatus(),
                order.getCustomerName()
        );
        rabbitTemplate.convertAndSend(NotificationMessagingConstants.ORDER_STATUS_EXCHANGE, NotificationMessagingConstants.ROUTING_KEY_STATUS, event);
        log.info("Published OrderStatusChangedEvent for order: {}, status: {}",
                order.getId(), order.getStatus());
    }
}

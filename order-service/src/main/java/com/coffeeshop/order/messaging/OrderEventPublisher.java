package com.coffeeshop.order.messaging;

import com.coffeeshop.messaging.OrderPlacedEvent;
import com.coffeeshop.messaging.OrderStatusChangedEvent;
import com.coffeeshop.order.model.Order;
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

        rabbitTemplate.convertAndSend("order.placed.exchange", "order.placed", event);
        rabbitTemplate.convertAndSend("order.placed.exchange", "order.placed", event);
        log.info("Published OrderPlacedEvent for order: {}", order.getId());
    }

    public void publishOrderStatusChanged(Order order){
        OrderStatusChangedEvent event = new OrderStatusChangedEvent(
                order.getId(),
                order.getStatus(),
                order.getCustomerName()
        );
        rabbitTemplate.convertAndSend("order.status.exchange", "order.status", event);
        log.info("Published OrderStatusChangedEvent for order: {}, status: {}",
                order.getId(), order.getStatus());
    }
}

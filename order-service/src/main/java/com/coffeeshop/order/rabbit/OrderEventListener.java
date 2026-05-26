package com.coffeeshop.order.rabbit;

import com.coffeeshop.rabbitmq.InventoryCheckEvent;
import com.coffeeshop.rabbitmq.OrderStatus;
import com.coffeeshop.order.model.Order;
import com.coffeeshop.order.service.OrderService;
import com.coffeeshop.rabbitmq.config.inventory.InventoryMessagingConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    private final OrderService orderService;
    private final OrderEventPublisher orderEventPublisher;

    @RabbitListener(queues = InventoryMessagingConstants.INVENTORY_RESPONSE_QUEUE)
    public void handleInventoryCheck(InventoryCheckEvent event){
        OrderStatus status = event.isAvailable() ? OrderStatus.AWAITING_PAYMENT : OrderStatus.CANCELLED;

        Order order = orderService.updateStatus(event.getOrderId(), status);
        log.info("Order {} status changed to {}", event.getOrderId(), status);
        orderEventPublisher.publishOrderStatusChanged(order);
    }
}

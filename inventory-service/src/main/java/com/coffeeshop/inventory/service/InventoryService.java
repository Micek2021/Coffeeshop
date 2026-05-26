package com.coffeeshop.inventory.service;

import com.coffeeshop.inventory.model.InventoryState;
import com.coffeeshop.inventory.repository.InventoryRepository;
import com.coffeeshop.rabbitmq.InventoryCheckEvent;
import com.coffeeshop.rabbitmq.OrderCompensationEvent;
import com.coffeeshop.rabbitmq.OrderPlacedEvent;
import com.coffeeshop.rabbitmq.config.inventory.InventoryMessagingConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = InventoryMessagingConstants.INVENTORY_QUEUE)
    @Transactional
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Processing OrderPlacedEvent for order ID: {}, product ID: {}", event.getOrderId(), event.getProductId());

        boolean isAvailable = false;
        var stateOptional = inventoryRepository.findByProductId(event.getProductId());

        if (stateOptional.isPresent()) {
            InventoryState inventoryState = stateOptional.get();
            if (inventoryState.getStock() >= event.getQuantity()) {
                inventoryState.setStock(inventoryState.getStock() - event.getQuantity());
                inventoryRepository.save(inventoryState);
                isAvailable = true;
            } else {
                log.warn("Insufficent stock for product ID: {}. Required: {}, Available: {}",
                        event.getProductId(), event.getQuantity(), inventoryState.getStock());
            }
        } else {
            log.error("Product ID: {} not found in inventory database", event.getProductId());
        }

        InventoryCheckEvent responseEvent = new InventoryCheckEvent(event.getOrderId(), isAvailable);
        rabbitTemplate.convertAndSend("", InventoryMessagingConstants.INVENTORY_RESPONSE_QUEUE, responseEvent);
    }

    @RabbitListener(queues = InventoryMessagingConstants.INVENTORY_COMPENSATION_QUEUE)
    @Transactional
    public void handleOrderCompensation(OrderCompensationEvent event) {
        log.info("Processing stock compensation for order ID: {}, product ID: {}", event.getOrderId(), event.getProductId());

        inventoryRepository.findByProductId(event.getProductId()).ifPresent(inventoryState -> {
            inventoryState.setStock(inventoryState.getStock() + event.getQuantity());
            inventoryRepository.save(inventoryState);
        });
    }
}
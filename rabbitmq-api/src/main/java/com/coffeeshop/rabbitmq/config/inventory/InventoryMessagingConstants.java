package com.coffeeshop.rabbitmq.config.inventory;

public final class InventoryMessagingConstants {
    public static final String ORDER_PLACED_EXCHANGE = "order.placed.exchange";
    public static final String INVENTORY_QUEUE = "inventory.queue";
    public static final String ROUTING_KEY_PLACED = "order.placed";

    public static final String INVENTORY_RESPONSE_QUEUE = "inventory.response.queue";
}

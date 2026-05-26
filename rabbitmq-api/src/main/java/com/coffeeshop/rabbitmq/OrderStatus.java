package com.coffeeshop.rabbitmq;


public enum OrderStatus {
    PENDING,
    AWAITING_PAYMENT,
    CONFIRMED,
    CANCELLED,
    SHIPPED,
    DELIVERED
}


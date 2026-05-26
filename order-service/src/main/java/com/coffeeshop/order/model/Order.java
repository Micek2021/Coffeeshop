package com.coffeeshop.order.model;

import com.coffeeshop.rabbitmq.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Long productId;
    private String productName;
    private String productImageUrl;
    private int quantity;
    private double totalPrice;
    private String customerName;
}

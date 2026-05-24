package com.coffeeshop.order.service;

import com.coffeeshop.grpc.ProductResponse;
import com.coffeeshop.order.messaging.OrderEventPublisher;
import com.coffeeshop.order.model.Order;
import com.coffeeshop.messaging.OrderStatus;
import com.coffeeshop.order.repository.OrderRepository;
import com.coffeeshop.order.grpc.ProductGrpcClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductGrpcClient productGrpcClient;

    private final OrderEventPublisher orderEventPublisher;

    public Order placeOrder(Long productId, int quantity, String customerName){
        ProductResponse product = productGrpcClient.getProduct(productId);

        Order order = new Order(
                null,
                OrderStatus.PENDING,
                productId,
                product.getName(),
                product.getImageUrl(),
                quantity,
                product.getPrice() * quantity,
                customerName
        );
        orderRepository.save(order);
        orderEventPublisher.publishOrderPlaced(order);

        log.info("Order placed: {}", order.getId());
        return order;
    }

    public Order payForOrder(Long orderId){
        return null;
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

    public java.util.List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = getOrder(orderId);
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}

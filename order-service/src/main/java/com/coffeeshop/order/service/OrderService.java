package com.coffeeshop.order.service;

import com.coffeeshop.grpc.ProductResponse;
import com.coffeeshop.order.rabbit.OrderEventPublisher;
import com.coffeeshop.order.model.Order;
import com.coffeeshop.rabbitmq.OrderStatus;
import com.coffeeshop.order.repository.OrderRepository;
import com.coffeeshop.order.grpc.ProductGrpcClient;
import com.coffeeshop.order.soap.PaymentSoapClient;
import com.coffeeshop.soap.PaymentRequest;
import com.coffeeshop.soap.PaymentResponse;
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
    private final PaymentSoapClient paymentClient;

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
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.AWAITING_PAYMENT) {
            throw new IllegalStateException("Order " + orderId + " cannot be paid in status: " + order.getStatus());
        }

        log.info("Processing SOAP payment for order {} via button click...", orderId);

        PaymentRequest soapRequest = new PaymentRequest();
        soapRequest.setOrderId(orderId);
        soapRequest.setTotalPrice(order.getTotalPrice());

        PaymentResponse soapResponse = paymentClient.processPayment(soapRequest);

        OrderStatus newStatus;
        if (soapResponse.isPaymentApproved()) {
            log.info("Payment APPROVED for order {}", orderId);
            newStatus = OrderStatus.CONFIRMED;
        } else {
            log.warn("Payment REJECTED for order {}", orderId);
            newStatus = OrderStatus.CANCELLED;
        }

        Order updatedOrder = updateStatus(orderId, newStatus);
        orderEventPublisher.publishOrderStatusChanged(updatedOrder);

        return updatedOrder;
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

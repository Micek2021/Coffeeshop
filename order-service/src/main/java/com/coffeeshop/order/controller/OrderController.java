package com.coffeeshop.order.controller;

import com.coffeeshop.messaging.OrderStatus;
import com.coffeeshop.order.model.Order;
import com.coffeeshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id){
        try {
            return ResponseEntity.ok(orderService.getOrder(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest request){
        Order order = orderService.placeOrder(
                request.getProductId(),
                request.getQuantity(),
                request.getCustomerName()
        );
        return ResponseEntity.accepted().body(order);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Order> payForOrder(@PathVariable Long id){
        Order order = orderService.payForOrder(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> changeStatus(@PathVariable Long id, @RequestBody OrderStatus status){
        Order order = orderService.updateStatus(id, status);
        return ResponseEntity.ok(order);
    }
}

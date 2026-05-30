package com.coffeeshop.order.controller;

import com.coffeeshop.rabbitmq.OrderStatus;
import com.coffeeshop.order.model.Order;
import com.coffeeshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public CollectionModel<EntityModel<Order>> getAllOrders() {
        List<EntityModel<Order>> orders = orderService.getAllOrders()
                .stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrder(id);
        return toModel(order);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Order>> placeOrder(@RequestBody OrderRequest request) {
        Order order = orderService.placeOrder(
                request.getProductId(),
                request.getQuantity(),
                request.getCustomerName()
        );
        return ResponseEntity.accepted().body(toModel(order));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<EntityModel<Order>> payForOrder(@PathVariable Long id) {
        Order order = orderService.payForOrder(id);
        return ResponseEntity.ok(toModel(order));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EntityModel<Order>> changeStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        Order order = orderService.updateStatus(id, status);
        return ResponseEntity.ok(toModel(order));
    }

    private EntityModel<Order> toModel(Order order) {
        EntityModel<Order> model = EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders")
        );

        if (order.getStatus() == OrderStatus.AWAITING_PAYMENT) {
            model.add(linkTo(methodOn(OrderController.class).payForOrder(order.getId())).withRel("pay"));
        }

        return model;
    }
}
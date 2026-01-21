package com.gustavo.order_service.controller;

import com.gustavo.order_service.dto.CreateOrderDto;
import com.gustavo.order_service.model.Order;
import com.gustavo.order_service.model.OrderStatus;
import com.gustavo.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid CreateOrderDto createOrderDto) {
        Order order = orderService.createOrder(createOrderDto);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable String orderId, @RequestBody OrderStatus status) {
        Order order = orderService.updateOrderStatus(UUID.fromString(orderId), status);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Order order = orderService.getOrderById(UUID.fromString(orderId));
        return ResponseEntity.ok(order);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable String userId) {
        List<Order> orders = orderService.getOrdersByUserId(UUID.fromString(userId));
        return ResponseEntity.ok(orders);
    }

}

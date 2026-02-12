package com.gustavo.order_service.controller;

import com.gustavo.order_service.dto.CreateOrderDto;
import com.gustavo.order_service.dto.CreateOrderResponse;
import com.gustavo.order_service.model.Order;
import com.gustavo.order_service.model.OrderStatus;
import com.gustavo.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderDto createOrderDto) {
        log.info("Received request to create order for userId: {}", createOrderDto.userId());
        var order = orderService.createOrder(createOrderDto);

        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable String orderId, @RequestBody OrderStatus status) {
        log.info("Received request to update order status for orderId: {} to status: {}", orderId, status);
        Order order = orderService.updateOrderStatus(UUID.fromString(orderId), status);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        log.info("Received request to get order with id: {}", orderId);
        Order order = orderService.getOrderById(UUID.fromString(orderId));
        return ResponseEntity.ok(order);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable String userId) {
        log.info("Received request to get orders for userId: {}", userId);
        List<Order> orders = orderService.getOrdersByUserId(UUID.fromString(userId));
        return ResponseEntity.ok(orders);
    }

}

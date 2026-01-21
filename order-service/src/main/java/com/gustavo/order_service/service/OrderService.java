package com.gustavo.order_service.service;

import com.gustavo.order_service.dto.CreateOrderDto;
import com.gustavo.order_service.exception.OrderNotFoundException;
import com.gustavo.order_service.model.Order;
import com.gustavo.order_service.model.OrderItem;
import com.gustavo.order_service.model.OrderStatus;
import com.gustavo.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(CreateOrderDto createOrderDto) {
        var order = new Order();
        order.setUserId(createOrderDto.userId());
        for(var itemDto : createOrderDto.items()) {
            var orderItem = new OrderItem();
            orderItem.setProductId(itemDto.productId());
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setUnitPrice(itemDto.unitPrice());
            orderItem.calculateTotalPrice();
            orderItem.setOrder(order);
            order.getItems().add(orderItem);
        }
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());
        order.calculateTotalAmount();
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(UUID orderId, OrderStatus status) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getUserId().equals(userId))
                .toList();
    }
}

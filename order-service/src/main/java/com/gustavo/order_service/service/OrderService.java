package com.gustavo.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.order_service.client.PaymentClient;
import com.gustavo.order_service.dto.CreateOrderDto;
import com.gustavo.order_service.dto.CreateOrderResponse;
import com.gustavo.order_service.dto.OrderItemDto;
import com.gustavo.order_service.dto.PaymentRequest;
import com.gustavo.order_service.event.JsonMessageOrderCreated;
import com.gustavo.order_service.exception.OrderNotFoundException;
import com.gustavo.order_service.model.Order;
import com.gustavo.order_service.model.OrderItem;
import com.gustavo.order_service.model.OrderStatus;
import com.gustavo.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentClient paymentClient;

    public void publishOrderCreatedEvent(Order order) {

        JsonMessageOrderCreated event = CreateJsonMessageOrder(order);

        kafkaTemplate.send("ORDER.CREATED", event);
    }
    @Transactional
    public void publishOrderStatusEvent(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        JsonMessageOrderCreated event = CreateJsonMessageOrder(order);
        String topic = switch (order.getStatus()) {
            case PAID -> "ORDER.PAID";
            case CANCELLED -> "ORDER.CANCELLED";
            default -> null;
        };
        kafkaTemplate.send(topic, event);
    }
    @Transactional
    public JsonMessageOrderCreated CreateJsonMessageOrder(Order order) {
        JsonMessageOrderCreated event = new JsonMessageOrderCreated();
        event.setOrderId(order.getOrderId());
        event.setUserId(order.getUserId());
        event.setItems(
                order.getItems()
                        .stream()
                        .map(item -> new OrderItemDto(
                                item.getProductId(),
                                item.getQuantity(),
                                item.getUnitPrice()
                        ))
                        .toList()
        );
        event.setTotalPrice(order.getTotalPrice());
        return event;
    }
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderDto createOrderDto) {
        var order = new Order();
        order.setUserId(createOrderDto.userId());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());

        for (var itemDto : createOrderDto.items()) {
            var orderItem = new OrderItem();
            orderItem.setProductId(itemDto.productId());
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setUnitPrice(itemDto.unitPrice());
            orderItem.calculateTotalPrice();
            orderItem.setOrder(order);
            order.getItems().add(orderItem);
        }

        order.calculateTotalAmount();
        Order saved = orderRepository.save(order);

        publishOrderCreatedEvent(saved);

        var req = new PaymentRequest((int) (saved.getTotalPrice()*100),"brl","Order #"+saved.getOrderId() );

        String checkoutUrl = paymentClient.createPayment(saved.getOrderId(), req);

        return new CreateOrderResponse(saved,checkoutUrl);
    }
    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus status) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);

        var saved = orderRepository.save(order);
        publishOrderStatusEvent(saved.getOrderId());
        return saved;
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

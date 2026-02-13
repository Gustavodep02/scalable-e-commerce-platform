package com.gustavo.order_service.service;

import com.gustavo.order_service.client.PaymentClient;
import com.gustavo.order_service.dto.CreateOrderDto;
import com.gustavo.order_service.dto.CreateOrderResponse;
import com.gustavo.order_service.dto.OrderItemDto;
import com.gustavo.order_service.exception.OrderNotFoundException;
import com.gustavo.order_service.model.Order;
import com.gustavo.order_service.model.OrderStatus;
import com.gustavo.order_service.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("Should create order successfully")
    void createOrderSuccessfully() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderItemDto item = new OrderItemDto(productId, 2, 50.0);
        CreateOrderDto createOrderDto = new CreateOrderDto(userId, List.of(item));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(UUID.randomUUID());
            return order;
        });
        when(paymentClient.createPayment(any(), any())).thenReturn("https://checkout.stripe.com/123");

        CreateOrderResponse response = orderService.createOrder(createOrderDto);

        assertNotNull(response);
        assertNotNull(response.order());
        assertEquals(userId, response.order().getUserId());
        assertEquals(OrderStatus.CREATED, response.order().getStatus());
    }

    @Test
    @DisplayName("Should get order by id successfully")
    void getOrderByIdSuccessfully() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
    }

    @Test
    @DisplayName("Should throw exception when order not found")
    void getOrderByIdThrowsWhenNotFound() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    @DisplayName("Should update order status successfully")
    void updateOrderStatusSuccessfully() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.updateOrderStatus(orderId, OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, result.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when updating status of non-existent order")
    void updateOrderStatusThrowsWhenNotFound() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrderStatus(orderId, OrderStatus.PAID));
    }

    @Test
    @DisplayName("Should get orders by user id")
    void getOrdersByUserIdSuccessfully() {
        UUID userId = UUID.randomUUID();
        Order order1 = new Order();
        order1.setUserId(userId);
        Order order2 = new Order();
        order2.setUserId(userId);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Order> result = orderService.getOrdersByUserId(userId);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should return empty list when user has no orders")
    void getOrdersByUserIdReturnsEmptyList() {
        UUID userId = UUID.randomUUID();

        when(orderRepository.findAll()).thenReturn(List.of());

        List<Order> result = orderService.getOrdersByUserId(userId);

        assertTrue(result.isEmpty());
    }
}

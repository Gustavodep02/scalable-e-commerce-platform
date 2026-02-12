package com.gustavo.order_service.listener;

import com.gustavo.order_service.event.JsonMessagePayment;
import com.gustavo.order_service.model.OrderStatus;
import com.gustavo.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentListener {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    public PaymentListener(OrderService orderService) {
        this.orderService = orderService;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "PAYMENT.SUCCEEDED", containerFactory = "kafkaListenerContainerFactory")
    public void consumePaymentSucceeded(String event) {
        log.info("Received PAYMENT.SUCCEEDED event: {}", event);
        try {
            JsonMessagePayment message = objectMapper.readValue(event, JsonMessagePayment.class);
            orderService.updateOrderStatus(message.getOrderId(), OrderStatus.PAID);
        } catch (Exception e) {
            log.error("Error converting JSON for PAYMENT.SUCCEEDED event: {}", e.getMessage());
        }
    }
    @KafkaListener(topics = "PAYMENT.FAILED", containerFactory = "kafkaListenerContainerFactory")
    public void consumePaymentFailed(String event) {
        try {
            log.info("Received PAYMENT.FAILED event: {}", event);
            JsonMessagePayment message = objectMapper.readValue(event, JsonMessagePayment.class);
            orderService.updateOrderStatus(message.getOrderId(), OrderStatus.CANCELLED);
        } catch (Exception e) {
            log.error("Error converting JSON for PAYMENT.FAILED event: {}", e.getMessage());
        }
    }
}

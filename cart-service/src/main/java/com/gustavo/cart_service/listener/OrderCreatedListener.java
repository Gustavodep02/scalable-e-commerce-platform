package com.gustavo.cart_service.listener;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gustavo.cart_service.model.JsonMessage;
import com.gustavo.cart_service.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreatedListener {

    private final ObjectMapper objectMapper;
    private final CartService cartService;

    public OrderCreatedListener(CartService cartService) {
        this.cartService = cartService;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "ORDER.CREATED", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderCreated(String event) {
        try {
            log.info("Received ORDER.CREATED event: {}", event);
            JsonMessage message = objectMapper.readValue(event, JsonMessage.class);
            cartService.clearCart(message.getUserId());
        } catch (Exception e) {
            log.error("Error converting JSON for ORDER.CREATED event: {}", e.getMessage());
        }
    }
}
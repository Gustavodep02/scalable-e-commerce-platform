package com.gustavo.notification_service.listener;

import com.gustavo.notification_service.model.JsonMessage;
import com.gustavo.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaSubListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "ORDER.CREATED", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderCreated(String event) {
        try {
            log.info("Received ORDER.CREATED event: {}", event);
            JsonMessage message = objectMapper.readValue(event, JsonMessage.class);
            notificationService.sendNotificationCreated(message);
        } catch (Exception e) {
            log.error("Error converting JSON for ORDER.CREATED event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "ORDER.CANCELLED", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderCancelled(String event) {
        try {
            log.info("Received ORDER.CANCELLED event: {}", event);
            JsonMessage message = objectMapper.readValue(event, JsonMessage.class);
            notificationService.sendNotificationCancelled(message);
        } catch (Exception e) {
            log.error("Error converting JSON for ORDER.CANCELLED event: {}", e.getMessage());
        }
    }
    @KafkaListener(topics = "ORDER.PAID", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderPaid(String event) {
        try {
            log.info("Received ORDER.PAID event: {}", event);
            JsonMessage message = objectMapper.readValue(event, JsonMessage.class);
            notificationService.sendNotificationPaid(message);
        } catch (Exception e) {
            log.error("Error converting JSON for ORDER.PAID event: {}", e.getMessage());
        }
    }
}

package com.gustavo.notification_service.listener;

import com.gustavo.notification_service.model.JsonMessage;
import com.gustavo.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


@Component
@RequiredArgsConstructor
public class KafkaSubListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "ORDER.CREATED", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderCreated(String event) {
        try {
            JsonMessage message = objectMapper.readValue(event, JsonMessage.class);
            notificationService.sendNotificationCreated(message);
        } catch (Exception e) {
            System.err.println("Erro ao converter JSON: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "ORDER.CANCELLED", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderCancelled(String event) {
        try {
            JsonMessage message = objectMapper.readValue(event, JsonMessage.class);
            notificationService.sendNotificationCancelled(message);
        } catch (Exception e) {
            System.err.println("Erro ao converter JSON: " + e.getMessage());
        }
    }
    @KafkaListener(topics = "ORDER.PAID", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderPaid(String event) {
        try {
            JsonMessage message = objectMapper.readValue(event, JsonMessage.class);
            notificationService.sendNotificationPaid(message);
        } catch (Exception e) {
            System.err.println("Erro ao converter JSON: " + e.getMessage());
        }
    }
}

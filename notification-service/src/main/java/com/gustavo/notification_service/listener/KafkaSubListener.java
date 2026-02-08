package com.gustavo.notification_service.listener;

import com.gustavo.notification_service.model.JsonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


@Component
@RequiredArgsConstructor
public class KafkaSubListener {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "ORDER.CREATED", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderCreated(String event) {
        try {
            JsonMessage message = objectMapper.readValue(event, JsonMessage.class);
            // Aqui você pode chamar o serviço de notificação para enviar uma notificação ao usuário
        } catch (Exception e) {
            System.err.println("Erro ao converter JSON: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "ORDER.CANCELLED", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderCancelled(String event) {
        try {
            JsonMessage message = objectMapper.readValue(event, JsonMessage.class);
            // Aqui você pode chamar o serviço de notificação para enviar uma notificação ao usuário
        } catch (Exception e) {
            System.err.println("Erro ao converter JSON: " + e.getMessage());
        }
    }
    @KafkaListener(topics = "ORDER.PAID", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderPaid(String event) {
        try {
            JsonMessage message = objectMapper.readValue(event, JsonMessage.class);
            // Aqui você pode chamar o serviço de notificação para enviar uma notificação ao usuário
        } catch (Exception e) {
            System.err.println("Erro ao converter JSON: " + e.getMessage());
        }
    }
}

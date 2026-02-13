package com.gustavo.notification_service.service;

import com.gustavo.notification_service.client.UserClient;
import com.gustavo.notification_service.dto.UserEmailDto;
import com.gustavo.notification_service.model.JsonMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("Should send cancelled notification")
    void sendNotificationCancelledSuccessfully() {
        UUID userId = UUID.randomUUID();
        JsonMessage message = new JsonMessage();
        message.setUserId(userId);

        when(userClient.getUser(userId)).thenReturn(new UserEmailDto("test@email.com"));

        notificationService.sendNotificationCancelled(message);

        verify(userClient).getUser(userId);
    }

    @Test
    @DisplayName("Should send paid notification")
    void sendNotificationPaidSuccessfully() {
        UUID userId = UUID.randomUUID();
        JsonMessage message = new JsonMessage();
        message.setUserId(userId);

        when(userClient.getUser(userId)).thenReturn(new UserEmailDto("test@email.com"));

        notificationService.sendNotificationPaid(message);

        verify(userClient).getUser(userId);
    }

    @Test
    @DisplayName("Should send created notification")
    void sendNotificationCreatedSuccessfully() {
        UUID userId = UUID.randomUUID();
        JsonMessage message = new JsonMessage();
        message.setUserId(userId);

        when(userClient.getUser(userId)).thenReturn(new UserEmailDto("test@email.com"));

        notificationService.sendNotificationCreated(message);

        verify(userClient).getUser(userId);
    }
}


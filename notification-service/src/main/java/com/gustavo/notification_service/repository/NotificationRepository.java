package com.gustavo.notification_service.repository;

import com.gustavo.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID > {
}

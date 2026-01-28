package com.gustavo.payment_service.repository;

import com.gustavo.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Payment findBySessionId(String sessionId);
    Payment findByOrderId(UUID orderId);
}

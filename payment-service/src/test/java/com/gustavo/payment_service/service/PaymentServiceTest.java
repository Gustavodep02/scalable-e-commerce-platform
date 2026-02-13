package com.gustavo.payment_service.service;

import com.gustavo.payment_service.dto.StripeCheckoutDto;
import com.gustavo.payment_service.gateway.PaymentGateway;
import com.gustavo.payment_service.model.Payment;
import com.gustavo.payment_service.model.PaymentStatus;
import com.gustavo.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("Should create checkout successfully")
    void createCheckoutSuccessfully() {
        UUID orderId = UUID.randomUUID();
        Long amount = 10000L;

        StripeCheckoutDto checkoutDto = new StripeCheckoutDto("session_123", "https://checkout.stripe.com/123");
        when(paymentGateway.createCheckout(orderId, amount)).thenReturn(checkoutDto);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.createCheckout(orderId, amount);

        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals(amount, result.getAmount());
        assertEquals(PaymentStatus.CREATED, result.getStatus());
    }

    @Test
    @DisplayName("Should mark payment as PAID")
    void markAsPaidSuccessfully() {
        String sessionId = "session_123";
        Payment payment = Payment.builder()
                .orderId(UUID.randomUUID())
                .sessionId(sessionId)
                .status(PaymentStatus.CREATED)
                .build();

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        paymentService.markAsPaid(sessionId);

        assertEquals(PaymentStatus.PAID, payment.getStatus());
    }

    @Test
    @DisplayName("Should mark payment as FAILED")
    void markAsFailedSuccessfully() {
        String sessionId = "session_456";
        Payment payment = Payment.builder()
                .orderId(UUID.randomUUID())
                .sessionId(sessionId)
                .status(PaymentStatus.CREATED)
                .build();

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        paymentService.markAsFailed(sessionId);

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when gateway fails")
    void createCheckoutThrowsWhenGatewayFails() {
        UUID orderId = UUID.randomUUID();
        Long amount = 10000L;

        when(paymentGateway.createCheckout(orderId, amount)).thenThrow(new RuntimeException("Gateway error"));

        assertThrows(RuntimeException.class, () -> paymentService.createCheckout(orderId, amount));
    }

    @Test
    @DisplayName("Should throw exception when session not found for markAsPaid")
    void markAsPaidThrowsWhenSessionNotFound() {
        String sessionId = "nonexistent";

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> paymentService.markAsPaid(sessionId));
    }

    @Test
    @DisplayName("Should throw exception when session not found for markAsFailed")
    void markAsFailedThrowsWhenSessionNotFound() {
        String sessionId = "nonexistent";

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> paymentService.markAsFailed(sessionId));
    }
}

package com.gustavo.payment_service.service;

import com.gustavo.payment_service.dto.StripeCheckoutDto;
import com.gustavo.payment_service.event.JsonMessage;
import com.gustavo.payment_service.gateway.PaymentGateway;
import com.gustavo.payment_service.model.Payment;
import com.gustavo.payment_service.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;

import static com.gustavo.payment_service.model.PaymentStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentEvent(Payment payment) {
        String topic = switch (payment.getStatus()) {
            case PAID -> "PAYMENT.SUCCEEDED";
            case FAILED -> "PAYMENT.FAILED";
            default -> null;
        };
        JsonMessage event = new JsonMessage();
        event.setOrderId(payment.getOrderId());
        if (topic != null) {
            log.info("Publishing event to topic {}: {}", topic, event);
            kafkaTemplate.send(topic, event);
        }
    }
    @Transactional
    public Payment createCheckout(UUID orderId, Long amount) {

        StripeCheckoutDto checkout =
                paymentGateway.createCheckout(orderId, amount);

        Payment payment = Payment.builder()
                .orderId(orderId)
                .sessionId(checkout.sessionId())
                .checkoutUrl(checkout.checkoutUrl())
                .amount(amount)
                .status(CREATED)
                .build();

        return paymentRepository.save(payment);
    }
    @Transactional
    public void markAsPaid(String sessionId) {
        log.info("Marking payment as PAID for sessionId: {}", sessionId);
        Payment payment = paymentRepository.findBySessionId(sessionId);
        payment.setStatus(PAID);
        var saved = paymentRepository.save(payment);
        publishPaymentEvent(saved);
    }
    @Transactional
    public void markAsFailed(String sessionId) {
        log.info("Marking payment as FAILED for sessionId: {}", sessionId);
        Payment payment = paymentRepository.findBySessionId(sessionId);
        payment.setStatus(FAILED);
        var saved = paymentRepository.save(payment);
        publishPaymentEvent(saved);

    }
}

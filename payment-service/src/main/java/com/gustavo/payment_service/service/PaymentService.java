package com.gustavo.payment_service.service;

import com.gustavo.payment_service.dto.StripeCheckoutDto;
import com.gustavo.payment_service.gateway.PaymentGateway;
import com.gustavo.payment_service.model.Payment;
import com.gustavo.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

import static com.gustavo.payment_service.model.PaymentStatus.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

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
    public void markAsPaid(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        payment.setStatus(PAID);
        paymentRepository.save(payment);
    }
    public void markAsFailed(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        payment.setStatus(FAILED);
        paymentRepository.save(payment);
    }
}

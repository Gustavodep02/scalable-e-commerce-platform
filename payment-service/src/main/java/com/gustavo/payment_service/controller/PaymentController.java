package com.gustavo.payment_service.controller;

import com.gustavo.payment_service.dto.CheckoutRequestDto;
import com.gustavo.payment_service.dto.CheckoutResponseDto;
import com.gustavo.payment_service.model.Payment;
import com.gustavo.payment_service.service.PaymentService;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeSecretKey;
    }
    @Value("${STRIPE_SK}")
    private String stripeSecretKey;

    @PostMapping("/{orderId}")
    public ResponseEntity<CheckoutResponseDto> checkout(
            @PathVariable UUID orderId,
            @RequestBody CheckoutRequestDto request
    ) {
        log.info("Received checkout request for orderId: {} with amount: {}", orderId, request.amount());
        Payment payment =
                paymentService.createCheckout(orderId, request.amount());

        return ResponseEntity.ok(
                new CheckoutResponseDto(
                        payment.getSessionId(),
                        payment.getCheckoutUrl()
                )
        );
    }
}

package com.gustavo.payment_service.controller;

import com.gustavo.payment_service.service.PaymentService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/webhooks/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping
    public ResponseEntity<Void> handle(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        switch (event.getType()) {

            case "checkout.session.completed" -> {
                var session = (Session)
                        event.getDataObjectDeserializer()
                                .getObject()
                                .orElseThrow();

                paymentService.markAsPaid(session.getId());
            }

            case "checkout.session.expired" -> {
                var session = (Session)
                        event.getDataObjectDeserializer()
                                .getObject()
                                .orElseThrow();

                paymentService.markAsFailed(session.getId());
            }
        }

        return ResponseEntity.ok().build();
    }

}


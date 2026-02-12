package com.gustavo.payment_service.infra.stripe;

import com.gustavo.payment_service.dto.StripeCheckoutDto;
import com.gustavo.payment_service.gateway.PaymentGateway;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.UUID;

@Slf4j
@Component
public class StripePaymentGateway implements PaymentGateway {

    @Override
    public StripeCheckoutDto createCheckout(UUID orderId, Long amount) {
        log.info("Creating Stripe checkout for orderId: {} with amount: {}", orderId, amount);
        SessionCreateParams params =
                SessionCreateParams.builder().setPaymentIntentData(
                                SessionCreateParams.PaymentIntentData.builder()
                                        .putMetadata("orderId", orderId.toString())
                                        .build())
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("https://teste.com/success")
                        .setCancelUrl("https://teste.com/cancel")
                        .setClientReferenceId(orderId.toString())
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("brl")
                                                        .setUnitAmount(amount)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Order " + orderId)
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        try {
            Session session = Session.create(params);
            return new StripeCheckoutDto(session.getId(), session.getUrl());
        } catch (Exception e) {
            log.error("Error creating Stripe checkout for orderId: {}", orderId, e);
            throw new RuntimeException("Error creating Stripe checkout", e);
        }
    }
}


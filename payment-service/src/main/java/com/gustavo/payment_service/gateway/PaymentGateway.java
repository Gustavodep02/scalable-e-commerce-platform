package com.gustavo.payment_service.gateway;

import com.gustavo.payment_service.dto.StripeCheckoutDto;

import java.util.UUID;

public interface PaymentGateway {
    StripeCheckoutDto createCheckout(UUID orderId, Long amount);
}


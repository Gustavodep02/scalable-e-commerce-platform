package com.gustavo.payment_service.dto;

public record StripeCheckoutDto(String sessionId,
                                String checkoutUrl) {
}

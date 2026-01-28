package com.gustavo.payment_service.dto;

public record CheckoutResponseDto(String sessionId,
                                  String checkoutUrl) {
}

package com.gustavo.payment_service.dto;

public record CheckoutRequestDto(Long amount,
                                 String currency,
                                 String description) {
}

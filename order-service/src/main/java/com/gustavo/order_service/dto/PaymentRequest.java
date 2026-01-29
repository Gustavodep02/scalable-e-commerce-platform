package com.gustavo.order_service.dto;

public record PaymentRequest(Integer amount,
                             String currency,
                             String description) {
}

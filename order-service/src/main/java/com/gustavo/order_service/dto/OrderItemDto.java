package com.gustavo.order_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItemDto(
        @NotNull UUID productId,
        @NotNull Integer quantity,
        @NotNull Double unitPrice
) {
}

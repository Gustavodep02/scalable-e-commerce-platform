package com.gustavo.cart_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CartItemDto(@NotNull UUID productId,@NotNull Integer quantity,@NotNull UUID userId) {
}

package com.gustavo.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderDto(@NotNull UUID userId,
                             @NotNull List<OrderItemDto> items,
                             @NotBlank String shippingAddress) {
}

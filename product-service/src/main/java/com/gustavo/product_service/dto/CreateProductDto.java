package com.gustavo.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductDto(@NotBlank String name, @NotBlank String description, @NotNull Double price, @NotNull Integer stock) {
}

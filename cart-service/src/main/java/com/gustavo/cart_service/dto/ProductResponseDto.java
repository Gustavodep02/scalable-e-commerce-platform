package com.gustavo.cart_service.dto;

import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String name,
        String description,
        Double price,
        Integer stock
) {}

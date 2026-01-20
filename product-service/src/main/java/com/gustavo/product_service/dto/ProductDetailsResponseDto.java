package com.gustavo.product_service.dto;

import java.util.UUID;

public record ProductDetailsResponseDto(
        UUID id,
        String name,
        String description,
        Double price,
        Integer stock
) {}

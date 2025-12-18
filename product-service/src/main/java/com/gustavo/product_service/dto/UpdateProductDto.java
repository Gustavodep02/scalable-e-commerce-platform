package com.gustavo.product_service.dto;

public record UpdateProductDto( String name,
                                String description,
                                Double price,
                                Integer stock) {
}

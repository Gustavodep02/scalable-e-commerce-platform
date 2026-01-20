package com.gustavo.cart_service.client;

import com.gustavo.cart_service.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "product-service",
        url = "http://product-service:8082"
)
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductResponseDto getProduct(@PathVariable UUID id);
}

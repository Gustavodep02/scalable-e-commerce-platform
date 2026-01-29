package com.gustavo.order_service.client;

import com.gustavo.order_service.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "payment-service",
        url = "http://payment-service:8085"
)
public interface PaymentClient {

    @PostMapping("/payments/{orderId}")
    String createPayment(
            @PathVariable UUID orderId,
            @RequestBody PaymentRequest request
    );
}


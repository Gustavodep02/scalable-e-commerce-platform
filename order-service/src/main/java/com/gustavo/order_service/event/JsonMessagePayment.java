package com.gustavo.order_service.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class JsonMessagePayment {
    @JsonProperty("orderId")
    private UUID orderId;
}
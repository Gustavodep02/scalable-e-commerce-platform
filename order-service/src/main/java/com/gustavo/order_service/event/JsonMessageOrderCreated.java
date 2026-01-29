package com.gustavo.order_service.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gustavo.order_service.dto.OrderItemDto;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@ToString
public class JsonMessageOrderCreated {
    @JsonProperty("orderId")
    private UUID orderId;
    @JsonProperty("userId")
    private UUID userId;
    @JsonProperty("items")
    private List<OrderItemDto> items;
    @JsonProperty("totalPrice")
    private Double totalPrice;
}

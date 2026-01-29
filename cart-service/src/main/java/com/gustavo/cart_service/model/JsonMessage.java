package com.gustavo.cart_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class JsonMessage {
    @JsonProperty("userId")
    private UUID userId;
}

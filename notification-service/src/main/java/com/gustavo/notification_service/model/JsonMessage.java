package com.gustavo.notification_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonMessage {
    @JsonProperty("userId")
    private UUID userId;
}


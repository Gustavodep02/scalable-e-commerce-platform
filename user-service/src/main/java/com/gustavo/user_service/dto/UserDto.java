package com.gustavo.user_service.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserDto (@NotBlank UUID id, String name, String email, String password) {
}

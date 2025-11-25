package com.gustavo.user_service.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterDto(@NotBlank String name, @NotBlank String email, @NotBlank String password) {
}

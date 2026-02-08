package com.gustavo.notification_service.client;

import com.gustavo.notification_service.config.FeignInternalAuthConfig;
import com.gustavo.notification_service.dto.UserEmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "user-service",
        url = "http://user-service:8081",
        configuration = FeignInternalAuthConfig.class
)
public interface UserClient {
    @GetMapping("/users/{userId}")
    UserEmailDto getUser(
            @PathVariable UUID userId
    );


}

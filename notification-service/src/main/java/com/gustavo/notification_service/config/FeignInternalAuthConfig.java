package com.gustavo.notification_service.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignInternalAuthConfig {

    private String internalToken = "notif-internal-123";

    @Bean
    public RequestInterceptor internalTokenInterceptor() {
        return request ->
                request.header("X-Internal-Token", internalToken);
    }
}



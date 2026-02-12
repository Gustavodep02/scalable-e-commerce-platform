package com.gustavo.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.core.convert.converter.Converter;

import javax.crypto.spec.SecretKeySpec;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    String jwtSecret;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/auth/**", "/eureka/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/products").hasAuthority("ADMIN")
                        .pathMatchers(HttpMethod.PATCH, "/products/**").hasAuthority("ADMIN")
                        .pathMatchers("/users/**").hasAuthority("ADMIN")
                        .pathMatchers("/orders/*/status").hasAuthority("ADMIN")
                        .pathMatchers("/webhooks/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .build();
    }


    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder
                .withSecretKey(
                        new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256")
                )
                .build();
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        return jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");

            Collection<SimpleGrantedAuthority> authorities =
                    roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        };
    }

}



package com.gustavo.cart_service.controller;

import com.gustavo.cart_service.model.Cart;
import com.gustavo.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getOrCreateCart(@PathVariable UUID userId) {
        log.info("Received request to get or create cart for userId: {}", userId);
        var cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{userId}/total-price")
    public ResponseEntity<Double> getCartTotalPrice(@PathVariable UUID userId) {
        log.info("Received request to get total price for cart of userId: {}", userId);
        var totalPrice = cartService.getCartTotalPrice(userId);
        return ResponseEntity.ok(totalPrice);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable UUID userId) {
        log.info("Received request to clear cart for userId: {}", userId);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}

package com.gustavo.cart_service.controller;

import com.gustavo.cart_service.model.Cart;
import com.gustavo.cart_service.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
@Tag(name= "carts", description = "Endpoints for managing shopping carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    @Operation(summary = "Retrieves or creates a cart for a user", description = "Returns the existing cart for the specified user ID, or creates a new cart if one does not already exist")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved or created cart")
    public ResponseEntity<Cart> getOrCreateCart(@PathVariable UUID userId) {
        log.info("Received request to get or create cart for userId: {}", userId);
        var cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{userId}/total-price")
    @Operation(summary = "Calculates total price of cart items", description = "Returns the total price of all items in the cart for the specified user ID")
    @ApiResponse(responseCode = "200", description = "Successfully calculated total price for cart")
    public ResponseEntity<Double> getCartTotalPrice(@PathVariable UUID userId) {
        log.info("Received request to get total price for cart of userId: {}", userId);
        var totalPrice = cartService.getCartTotalPrice(userId);
        return ResponseEntity.ok(totalPrice);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Clears the cart for a user", description = "Removes all items from the cart associated with the specified user ID")
    @ApiResponse(responseCode = "204", description = "Successfully cleared cart")
    public ResponseEntity<Void> clearCart(@PathVariable UUID userId) {
        log.info("Received request to clear cart for userId: {}", userId);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}

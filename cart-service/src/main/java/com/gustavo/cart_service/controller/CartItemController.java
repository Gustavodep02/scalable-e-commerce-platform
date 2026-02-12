package com.gustavo.cart_service.controller;


import com.gustavo.cart_service.dto.CartItemDto;
import com.gustavo.cart_service.model.CartItem;
import com.gustavo.cart_service.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart-items")
@Tag(name= "cart-items", description = "Endpoints for managing items in the shopping cart")
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping()
    @Operation(summary = "Adds an item to the cart", description = "Adds a specified quantity of a product to the user's cart and returns the created cart item")
    @ApiResponse(responseCode = "200", description = "Successfully added item to cart")
    public ResponseEntity<CartItem> addCartItem(@RequestBody @Valid CartItemDto cartItemDto) {
        log.info("Received request to add item to cart for userId: {} and productId: {} with quantity: {}",
                cartItemDto.userId(), cartItemDto.productId(), cartItemDto.quantity());
        var cartItem = cartItemService.addItemToCart(
                cartItemDto.productId(),
                cartItemDto.userId(),
                cartItemDto.quantity()
        );
        return ResponseEntity.ok(cartItem);
    }

    @PutMapping()
    @Operation(summary = "Updates the quantity of an item in the cart", description = "Updates the quantity of a specified product in the user's cart and returns the updated cart item")
    @ApiResponse(responseCode = "200", description = "Successfully updated item quantity in cart")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody @Valid CartItemDto cartItemDto) {
        log.info("Received request to update cart item for userId: {} and productId: {} with new quantity: {}",
                cartItemDto.userId(), cartItemDto.productId(), cartItemDto.quantity());
        var cartItem = cartItemService.updateCartItem(
                cartItemDto.productId(),
                cartItemDto.userId(),
                cartItemDto.quantity()
        );
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/{productId}/carts/{userId}")
    @Operation(summary = "Removes an item from the cart", description = "Removes a specified product from the user's cart")
    @ApiResponse(responseCode = "204", description = "Successfully removed item from cart")
    public ResponseEntity<Void> removeCartItem(@PathVariable("productId")  String productId,
                                               @PathVariable("userId") String userId) {
        log.info("Received request to remove item from cart for userId: {} and productId: {}", userId, productId);
        cartItemService.removeItemFromCart(
                java.util.UUID.fromString(productId),
                java.util.UUID.fromString(userId)
        );
        return ResponseEntity.noContent().build();
    }
}

package com.gustavo.cart_service.controller;


import com.gustavo.cart_service.dto.CartItemDto;
import com.gustavo.cart_service.model.CartItem;
import com.gustavo.cart_service.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping()
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

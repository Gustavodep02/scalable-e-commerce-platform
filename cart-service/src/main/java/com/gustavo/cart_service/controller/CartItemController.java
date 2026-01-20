package com.gustavo.cart_service.controller;


import com.gustavo.cart_service.dto.CartItemDto;
import com.gustavo.cart_service.model.CartItem;
import com.gustavo.cart_service.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping()
    public ResponseEntity<CartItem> addCartItem(@RequestBody @Valid CartItemDto cartItemDto) {
        var cartItem = cartItemService.addItemToCart(
                cartItemDto.productId(),
                cartItemDto.userId(),
                cartItemDto.quantity()
        );
        return ResponseEntity.ok(cartItem);
    }

    @PutMapping()
    public ResponseEntity<CartItem> updateCartItem(@RequestBody @Valid CartItemDto cartItemDto) {
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
        cartItemService.removeItemFromCart(
                java.util.UUID.fromString(productId),
                java.util.UUID.fromString(userId)
        );
        return ResponseEntity.noContent().build();
    }
}

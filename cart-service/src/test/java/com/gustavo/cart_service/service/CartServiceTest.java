package com.gustavo.cart_service.service;

import com.gustavo.cart_service.model.Cart;
import com.gustavo.cart_service.model.CartItem;
import com.gustavo.cart_service.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("Should return existing cart for user")
    void getOrCreateCartReturnsExistingCart() {
        UUID userId = UUID.randomUUID();
        Cart existingCart = new Cart(UUID.randomUUID(), userId, 100.0, new HashSet<>());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));

        Cart result = cartService.getOrCreateCart(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    @DisplayName("Should create new cart when user has no cart")
    void getOrCreateCartCreatesNewCart() {
        UUID userId = UUID.randomUUID();
        Cart newCart = new Cart(UUID.randomUUID(), userId, 0.0, new HashSet<>());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        Cart result = cartService.getOrCreateCart(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(0.0, result.getTotalPrice());
    }

    @Test
    @DisplayName("Should clear cart items")
    void clearCartSuccessfully() {
        UUID userId = UUID.randomUUID();
        HashSet<CartItem> items = new HashSet<>();
        items.add(new CartItem());
        Cart cart = new Cart(UUID.randomUUID(), userId, 50.0, items);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.clearCart(userId);

        assertTrue(cart.getItems().isEmpty());
        assertEquals(0.0, cart.getTotalPrice());
    }

    @Test
    @DisplayName("Should return cart total price")
    void getCartTotalPriceSuccessfully() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, 150.0, new HashSet<>());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Double totalPrice = cartService.getCartTotalPrice(userId);

        assertEquals(0.0, totalPrice);
    }

    @Test
    @DisplayName("Should return zero for empty cart")
    void getCartTotalPriceReturnsZeroForEmptyCart() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, 0.0, new HashSet<>());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Double totalPrice = cartService.getCartTotalPrice(userId);

        assertEquals(0.0, totalPrice);
    }
}


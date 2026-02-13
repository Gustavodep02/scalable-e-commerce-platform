package com.gustavo.cart_service.service;

import com.gustavo.cart_service.client.ProductClient;
import com.gustavo.cart_service.dto.ProductResponseDto;
import com.gustavo.cart_service.exception.OutOfStockException;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

    @Mock
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private CartItemService cartItemService;

    @Test
    @DisplayName("Should add new item to cart")
    void addItemToCartSuccessfully() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, 0.0, new HashSet<>());
        ProductResponseDto product = new ProductResponseDto(productId, "Product", "Description", 50.0, 10);

        when(cartService.getOrCreateCart(userId)).thenReturn(cart);
        when(productClient.getProduct(productId)).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartItem result = cartItemService.addItemToCart(productId, userId, 2);

        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals(2, result.getQuantity());
    }

    @Test
    @DisplayName("Should throw exception when stock is insufficient")
    void addItemToCartThrowsWhenOutOfStock() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, 0.0, new HashSet<>());
        ProductResponseDto product = new ProductResponseDto(productId, "Product", "Description", 50.0, 1);

        when(cartService.getOrCreateCart(userId)).thenReturn(cart);
        when(productClient.getProduct(productId)).thenReturn(product);

        assertThrows(OutOfStockException.class, () -> cartItemService.addItemToCart(productId, userId, 5));
    }

    @Test
    @DisplayName("Should remove item from cart")
    void removeItemFromCartSuccessfully() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        HashSet<CartItem> items = new HashSet<>();
        items.add(cartItem);
        Cart cart = new Cart(UUID.randomUUID(), userId, 50.0, items);

        when(cartService.getOrCreateCart(userId)).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartItemService.removeItemFromCart(productId, userId);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent item")
    void removeItemFromCartThrowsWhenNotFound() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, 0.0, new HashSet<>());

        when(cartService.getOrCreateCart(userId)).thenReturn(cart);

        assertThrows(OutOfStockException.class, () -> cartItemService.removeItemFromCart(productId, userId));
    }

    @Test
    @DisplayName("Should update item quantity in cart")
    void updateCartItemSuccessfully() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setQuantity(2);
        HashSet<CartItem> items = new HashSet<>();
        items.add(cartItem);
        Cart cart = new Cart(UUID.randomUUID(), userId, 100.0, items);
        ProductResponseDto product = new ProductResponseDto(productId, "Product", "Description", 50.0, 10);

        when(cartService.getOrCreateCart(userId)).thenReturn(cart);
        when(productClient.getProduct(productId)).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartItem result = cartItemService.updateCartItem(productId, userId, 5);

        assertEquals(5, result.getQuantity());
    }

    @Test
    @DisplayName("Should throw exception when updating with insufficient stock")
    void updateCartItemThrowsWhenOutOfStock() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setQuantity(2);
        HashSet<CartItem> items = new HashSet<>();
        items.add(cartItem);
        Cart cart = new Cart(UUID.randomUUID(), userId, 100.0, items);
        ProductResponseDto product = new ProductResponseDto(productId, "Product", "Description", 50.0, 3);

        when(cartService.getOrCreateCart(userId)).thenReturn(cart);
        when(productClient.getProduct(productId)).thenReturn(product);

        assertThrows(OutOfStockException.class, () -> cartItemService.updateCartItem(productId, userId, 10));
    }
}


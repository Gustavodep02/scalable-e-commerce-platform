package com.gustavo.cart_service.service;

import com.gustavo.cart_service.client.ProductClient;
import com.gustavo.cart_service.dto.ProductResponseDto;
import com.gustavo.cart_service.exception.OutOfStockException;
import com.gustavo.cart_service.model.Cart;
import com.gustavo.cart_service.model.CartItem;
import com.gustavo.cart_service.repository.CartItemRepository;
import com.gustavo.cart_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartService cartService;

    private final CartRepository cartRepository;

    private final ProductClient productClient;

    public CartItem addItemToCart(UUID productId, UUID userId, Integer quantity) {
        Cart cart = cartService.getOrCreateCart(userId);
        ProductResponseDto product = productClient.getProduct(productId);

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setProductId(productId);
                    newItem.setCart(cart);
                    cart.getItems().add(newItem);
                    return newItem;
                });
        if (product.stock() < (cartItem.getQuantity() + quantity)
        ) {
            log.error("Insufficient stock adding to cart for product: {}. Requested: {}, Available: {}",
                    product.name(), cartItem.getQuantity() + quantity, product.stock());
            throw new OutOfStockException("Insufficient stock for product: " + product.name());

        }
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setUnitPrice(product.price());
        cartItem.setTotalPrice();

        cart.getTotalPrice();
        cartRepository.save(cart);
        return cartItem;
    }
    public void removeItemFromCart(UUID productId, UUID userId) {
        Cart cart = cartService.getOrCreateCart(userId);
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new OutOfStockException("Product not found in cart"));

        cart.getItems().remove(cartItem);
        cart.getTotalPrice();
        cartRepository.save(cart);
    }

    public CartItem updateCartItem(UUID productId, UUID userId, Integer quantity) {
        Cart cart = cartService.getOrCreateCart(userId);
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new OutOfStockException("Product not found in cart"));

        if (quantity <= 0) {
            cart.getItems().remove(cartItem);
        } else {
            ProductResponseDto product = productClient.getProduct(productId);
            if (product.stock() < quantity) {
                log.error("Insufficient stock updating cart item for product: {}. Requested: {}, Available: {}",
                        product.name(), quantity, product.stock());
                throw new OutOfStockException("Insufficient stock for product: " + product.name());
            }
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.price());
            cartItem.setTotalPrice();
        }
        cart.getTotalPrice();
        cartRepository.save(cart);
        return cartItem;
    }
}

package com.gustavo.cart_service.service;

import com.gustavo.cart_service.model.Cart;
import com.gustavo.cart_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart getOrCreateCart(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(new Cart(null, userId, 0.0, new HashSet<>())));
    }

    public void clearCart(UUID userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }

    public Double getCartTotalPrice(UUID userId) {
        Cart cart = getOrCreateCart(userId);
        return cart.getTotalPrice();
    }
}

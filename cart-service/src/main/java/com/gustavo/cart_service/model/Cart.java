package com.gustavo.cart_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true, nullable = false)
    private UUID userId;
    private Double totalPrice;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        updateTotalPrice();
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        updateTotalPrice();
    }
    private void updateTotalPrice(){
        this.totalPrice = items.stream().map(item -> {
            Double unitPrice = item.getUnitPrice();
            if(unitPrice == null) return 0.0;
            return unitPrice * item.getQuantity();
        }).reduce(0.0, Double::sum);
    }

    public Double getTotalPrice() {
        updateTotalPrice();
        return totalPrice;
    }
}

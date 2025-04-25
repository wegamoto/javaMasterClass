package com.wewe.weweShop.service;

import com.wewe.weweShop.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    private final Map<String, List<CartItem>> userCarts = new HashMap<>();

    public List<CartItem> getCart(String email) {
        return userCarts.getOrDefault(email, new ArrayList<>());
    }

    public void addToCart(String email, CartItem item) {
        List<CartItem> cart = userCarts.computeIfAbsent(email, k -> new ArrayList<>());

        for (CartItem cartItem : cart) {
            if (cartItem.getProductId().equals(item.getProductId())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }

        cart.add(item);
    }

    public void clearCart(String email) {
        userCarts.remove(email);
    }

    public void removeFromCart(String email, Long productId) {
        List<CartItem> cart = getCart(email);
        cart.removeIf(item -> item.getProductId().equals(productId));
    }

    public void updateQuantity(String email, Long productId, int quantity) {
        List<CartItem> cart = getCart(email);
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                break;
            }
        }
    }
}



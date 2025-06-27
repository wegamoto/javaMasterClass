package com.wewe.temjaiShop.service;


import com.wewe.temjaiShop.model.CartItem;
import com.wewe.temjaiShop.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemCountServiceImpl implements CartItemCountService {

    private final CartItemRepository cartItemRepository;

    public CartItemCountServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public int getTotalItemCountByUsername(String userEmail) {
        List<CartItem> cartItems = cartItemRepository.findByUser_Email(userEmail);
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}


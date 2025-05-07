package com.wewe.weweShop.service;


import com.wewe.weweShop.model.CartItem;
import com.wewe.weweShop.repository.CartItemRepository;
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
        List<CartItem> cartItems = cartItemRepository.findByUserEmail(userEmail);
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}


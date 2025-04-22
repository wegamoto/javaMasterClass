package com.wewe.weweRestaurant.service;

import com.wewe.weweRestaurant.model.MenuItem;
import com.wewe.weweRestaurant.model.Order;
import com.wewe.weweRestaurant.model.OrderItem;
import com.wewe.weweRestaurant.repository.MenuItemRepository;
import com.wewe.weweRestaurant.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final MenuItemRepository menuItemRepo;

    public Order createOrder(Map<Long, Integer> menuItemQuantities, String tableNumber) {
        Order order = new Order();
        order.setTableNumber(tableNumber);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : menuItemQuantities.entrySet()) {
            MenuItem menuItem = menuItemRepo.findById(entry.getKey()).orElseThrow();
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(entry.getValue());
            items.add(orderItem);
        }

        order.setItems(items);
        return orderRepo.save(order);
    }
}


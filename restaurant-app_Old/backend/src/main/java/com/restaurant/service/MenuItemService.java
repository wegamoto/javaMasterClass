package com.restaurant.service;

import com.restaurant.model.Order;
import com.restaurant.model.OrderItem;
import com.restaurant.model.OrderStatus;
import com.restaurant.repository.OrderItemRepository;
import com.restaurant.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll(); // ดึงข้อมูลทั้งหมดจากตาราง MenuItem
    }

    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id).orElse(null); // ค้นหาด้วย ID
    }

    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem); // บันทึกข้อมูลเมนู
    }

    public List<MenuItem> searchMenuByName(String name) {
        return menuItemRepository.findByName(name); // ค้นหาจากชื่อเมนู
    }
}


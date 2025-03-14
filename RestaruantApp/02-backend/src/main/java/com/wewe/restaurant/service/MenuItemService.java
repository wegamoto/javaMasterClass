package com.wewe.restaurant.service;

import com.wewe.restaurant.model.MenuItem;
import com.wewe.restaurant.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    // ดึงรายการเมนูทั้งหมด
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    // ดึงเมนูตาม ID
    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }

    // เพิ่มเมนูใหม่
    public MenuItem createMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    // อัปเดตเมนู
    public MenuItem updateMenuItem(Long id, MenuItem updatedMenuItem) {
        return menuItemRepository.findById(id).map(menuItem -> {
            menuItem.setName(updatedMenuItem.getName());
            menuItem.setPrice(updatedMenuItem.getPrice());
            menuItem.setDescription(updatedMenuItem.getDescription());
            menuItem.setCategory(updatedMenuItem.getCategory());
            return menuItemRepository.save(menuItem);
        }).orElseThrow(() -> new RuntimeException("MenuItem not found with id: " + id));
    }

    // ลบเมนูตาม ID
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}


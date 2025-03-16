package com.wewe.restaurant.service;

import com.wewe.restaurant.model.Menu;
import com.wewe.restaurant.model.MenuItem;
import com.wewe.restaurant.repository.MenuItemRepository;
import com.wewe.restaurant.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    @Autowired
    private final MenuItemRepository menuItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

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
        // ตรวจสอบว่า Menu มีอยู่จริง
        Long menuId = menuItem.getMenu().getId();
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + menuId));

        menuItem.setMenu(menu);
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


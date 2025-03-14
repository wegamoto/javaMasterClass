package com.wewe.restaurant.service;

import com.wewe.restaurant.model.Menu;
import com.wewe.restaurant.model.Restaurant;
import com.wewe.restaurant.repository.MenuRepository;
import com.wewe.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    public MenuService(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    // 📌 ดึงรายการเมนูทั้งหมด
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    // 📌 ดึงเมนูตาม ID
    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    // 📌 ฟังก์ชันสร้างเมนูใหม่
    public Menu createMenu(Long restaurantId, Menu menu) {
        // ค้นหาจาก restaurantId ที่ส่งมา
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));

        // ตั้งค่าร้านอาหารที่ค้นพบให้กับเมนู
        menu.setRestaurant(restaurant);

        // บันทึกเมนูใหม่
        return menuRepository.save(menu);
    }

    // 📌 อัปเดตเมนู
    public Menu updateMenu(Long id, Menu updatedMenu) {
        return menuRepository.findById(id).map(menu -> {
            menu.setName(updatedMenu.getName());
            return menuRepository.save(menu);
        }).orElseThrow(() -> new RuntimeException("Menu not found"));
    }

    // 📌 ลบเมนู
    public void deleteMenu(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new RuntimeException("Menu not found");
        }
        menuRepository.deleteById(id);
    }

}


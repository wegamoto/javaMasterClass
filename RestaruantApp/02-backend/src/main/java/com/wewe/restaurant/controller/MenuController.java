package com.wewe.restaurant.controller;

import com.wewe.restaurant.model.Menu;
import com.wewe.restaurant.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // Use this for creating a menu with restaurantId as a request parameter
    @PostMapping("/menus/create")
    public ResponseEntity<Menu> createMenu(@RequestParam Long restaurantId, @RequestBody Menu menu) {
        Menu createdMenu = menuService.createMenu(restaurantId, menu);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenu);
    }

    // 📌 ดึงเมนูทั้งหมด
    @GetMapping("/menus")
    public ResponseEntity<List<Menu>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    // 📌 ดึงเมนูตาม id
    @GetMapping("/menus/{id}")
    public ResponseEntity<Optional<Menu>> getMenuById(@PathVariable Long id) {
        Optional<Menu> menu = menuService.getMenuById(id);
        return ResponseEntity.ok(menu);
    }

    // 📌 อัพเดตเมนู
    @PutMapping("/menus/{id}")
    public ResponseEntity<Menu> updateMenu(@PathVariable Long id, @RequestBody Menu menuDetails) {
        Menu updatedMenu = menuService.updateMenu(id, menuDetails);
        return ResponseEntity.ok(updatedMenu);
    }

    // 📌 ลบเมนู
    @DeleteMapping("/menus/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}

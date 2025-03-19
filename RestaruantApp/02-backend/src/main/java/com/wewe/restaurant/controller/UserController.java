package com.wewe.restaurant.controller;

import com.wewe.restaurant.model.User;
import com.wewe.restaurant.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ สร้างผู้ใช้ใหม่
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // ส่งข้อความผิดพลาดถ้าชื่อเป็น null หรือค่าว่าง
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    // ✅ ดึงรายชื่อผู้ใช้ทั้งหมด
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ✅ ดึงข้อมูลผู้ใช้ตาม ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // ✅ อัปเดตข้อมูลผู้ใช้
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    // ✅ ลบผู้ใช้
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}


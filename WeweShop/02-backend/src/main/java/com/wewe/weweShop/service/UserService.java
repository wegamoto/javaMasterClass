package com.wewe.weweShop.service;

import com.wewe.weweShop.model.User;
import com.wewe.weweShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByEmail(Principal principal) {
        // ตรวจสอบว่า principal ไม่เป็น null
        if (principal == null) {
            throw new IllegalArgumentException("Principal is null, user may not be authenticated.");
        }

        String userEmail = principal.getName();
        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException("User email is null or empty.");
        }
        // ค้นหาผู้ใช้จาก email
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
    }

    public User getUserByUsernameOrEmail(Principal principal) {
        String identifier = principal.getName(); // principal.getName() จะคืนค่าเป็น username หรือ email ขึ้นอยู่กับที่ใช้

        return userRepository.findByUsernameOrEmail(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + identifier));
    }

    // อัพเดตข้อมูลผู้ใช้
    public User updateProfile(String userEmail, User updatedUser) {
        User existingUser = userRepository.findByUsernameOrEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // อัพเดทข้อมูลผู้ใช้
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber()); // ตัวอย่างการอัพเดทข้อมูลเพิ่มเติม
        existingUser.setAddress(updatedUser.getAddress());

        // บันทึกการเปลี่ยนแปลงในฐานข้อมูล
        return userRepository.save(existingUser);
    }

    public void deleteUserByEmail(Principal principal) {
        User user = getUserByEmail(principal);
        userRepository.delete(user);
    }
}


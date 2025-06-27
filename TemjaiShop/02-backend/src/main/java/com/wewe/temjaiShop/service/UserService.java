package com.wewe.temjaiShop.service;

import com.wewe.temjaiShop.model.Role;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.model.enums.RoleName;
import com.wewe.temjaiShop.repository.RoleRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // ตรวจสอบว่า username นี้มีอยู่ในระบบหรือไม่
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        // กำหนด ROLE_USER เป็นค่าเริ่มต้น ถ้าไม่ได้กำหนดมา
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found."));
            user.setRoles(Set.of(defaultRole));
        }
        return userRepository.save(user);
    }

    public User getUserByEmail(Principal principal) {
        // ตรวจสอบว่า principal ไม่เป็น null
        if (principal == null) {
            throw new IllegalArgumentException("Principal is null, user may not be authenticated.");
        }

        String userEmail = extractEmailFromPrincipal(principal);

        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException("User email is null or empty.");
        }
        // ค้นหาผู้ใช้จาก email หรือ username ให้รองรับทั้งสอบแบบ
        return userRepository.findByUsernameOrEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByUsernameOrEmail(email);
    }

    public User getUserByUsernameOrEmail(String userEmail) {
        String identifier = userEmail; // principal.getName() จะคืนค่าเป็น username หรือ email ขึ้นอยู่กับที่ใช้

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

    private String extractEmailFromPrincipal(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauth2Token) {
            OAuth2User oauth2User = oauth2Token.getPrincipal();
            Object emailAttr = oauth2User.getAttributes().get("email");
            if (emailAttr != null) {
                return emailAttr.toString();
            } else {
                throw new IllegalStateException("OAuth2 user does not have an email attribute.");
            }
        }

        return principal.getName(); // สำหรับ local user (email เป็น username)
    }

    public void fixNullRoles() {
//        Set<RoleName> defaultRoles = Set.of(RoleName.ROLE_USER);
//        int updatedRows = userRepository.updateNullRoles(defaultRoles);
//        System.out.println("Updated roles for " + updatedRows + " users.");

        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found"));

        List<User> usersWithNullRoles = userRepository.findAll().stream()
                .filter(user -> user.getRoles() == null || user.getRoles().isEmpty())
                .toList();

        for (User user : usersWithNullRoles) {
            user.setRoles(Set.of(defaultRole));
            userRepository.save(user);
        }

        System.out.println("Updated " + usersWithNullRoles.size() + " users with ROLE_USER");
    }

    public void fixNullPhoneNumber() {
        int updatedRows = userRepository.updateNullPhoneNumber("0000000000");
        System.out.println("Updated phoneNumber for " + updatedRows + " users.");
    }

}


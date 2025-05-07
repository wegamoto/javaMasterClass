package com.wewe.weweShop.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.wewe.weweShop.model.User;  // Import Entity User ของคุณ

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    // Getters
    @Getter
    private Long id;                // ID ของผู้ใช้
    private String email;           // อีเมลของผู้ใช้
    private String password;        // รหัสผ่านของผู้ใช้
    private Collection<? extends GrantedAuthority> authorities;  // บทบาทของผู้ใช้ (roles)

    // Constructor
    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        // หาก User entity ของคุณมีการกำหนดบทบาท (roles) ก็ให้เอามาใช้ในส่วนนี้
        this.authorities = user.getAuthorities();  // สมมติว่า `User` มี method `getRoles()` คืนค่าบทบาท
    }

    @Override
    public String getUsername() {
        return email;  // ใช้ email เป็น username
    }

    @Override
    public String getPassword() {
        return password;  // คืนค่ารหัสผ่าน
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;  // คืนค่าบทบาทของผู้ใช้
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // กำหนดว่าไม่หมดอายุ
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // กำหนดว่าไม่ถูกล็อค
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // กำหนดว่าไม่มีการหมดอายุของรหัสผ่าน
    }

    @Override
    public boolean isEnabled() {
        return true;  // กำหนดว่าใช้งานได้
    }
}

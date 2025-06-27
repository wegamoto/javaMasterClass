package com.wewe.eduexam.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")  // ✅ เปลี่ยนจาก user เป็น users
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User implements UserDetails {

    // ===== Getter และ Setter =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String firstName; // ✅ เพิ่ม firstName

    private String lastName;  // ✅ เพิ่ม lastName

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student student;

    // ✅ ใช้สำหรับแสดงชื่อเต็ม
    public String getStudentFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        } else {
            return username; // fallback หากไม่มีชื่อ
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // ===== Implement methods from UserDetails =====
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // หรือใส่ Role ถ้าต้องการ
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


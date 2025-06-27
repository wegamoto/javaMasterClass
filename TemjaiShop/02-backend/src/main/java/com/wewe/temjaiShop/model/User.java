package com.wewe.temjaiShop.model;

import com.wewe.temjaiShop.model.enums.AuthProvider;
import com.wewe.temjaiShop.model.enums.RoleName;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users") // ตั้งชื่อตารางให้ปลอดภัยจากคำสงวน
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // <== ต้องใช้ field นี้เป็น credential login (เช่น principal.getName())

    private String name;

    private String username;

    private String fullName;

    private String password;

    private boolean enabled;

    @Column(name = "phone_number") // เพิ่มบรรทัดนี้
    private String phoneNumber;

    @Column
    private String address;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    String providerId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "favorite_category")
    private String favoriteCategory;

    // --- Getter/Setter ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    } // Getter

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    } // Setter

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getFavoriteCategory() {
        return favoriteCategory;
    }

    public void setFavoriteCategory(String favoriteCategory) {
        this.favoriteCategory = favoriteCategory;
    }

    // เพิ่มไว้ท้าย class User (ก่อนปิดคลาสด้วย '}')
    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
            user.setEnabled(true); // ตั้งค่า default ถ้าต้องการ
        }

        public Builder setUsername(String username) {
            user.setUsername(username);
            return this;
        }

        public Builder setEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder setFullName(String fullName) {
            user.setFullName(fullName);
            return this;
        }

        public Builder setPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder setRoles(Set<Role> roles) {
            user.setRoles(roles);
            return this;
        }

        public Builder setProvider(AuthProvider provider) {
            user.setProvider(provider);
            return this;
        }

        public Builder setEnabled(boolean enabled) {
            user.setEnabled(enabled);
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            user.setPhoneNumber(phoneNumber);
            return this;
        }

        public Builder setAddress(String address) {
            user.setAddress(address);
            return this;
        }

        public User build() {
            return user;
        }
    }

}

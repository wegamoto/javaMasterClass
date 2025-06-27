package com.wewe.weweShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimbusds.oauth2.sdk.TokenIntrospectionSuccessResponse;
import com.wewe.weweShop.dto.AuthRequest;
import com.wewe.weweShop.model.enums.AuthProvider;
import io.swagger.v3.oas.annotations.info.Contact;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String fullName;
    private AuthProvider provider;
    private String phoneNumber;
    private String address;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    private boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;

    // ===== Constructors =====
    public User() {
    }

    public User(Long id, String username, String email, String fullName, AuthProvider provider,
                String phoneNumber, String address, String password, List<String> roles, boolean enabled,
                List<Order> orders) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.provider = provider;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
        this.orders = orders;
    }

    public User(String username, String email, String password, List<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // ===== Getters and Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    // ===== UserDetails Interface Methods =====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles != null && !roles.isEmpty()) {
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // หรือปรับ logic ตามระบบ
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // หรือปรับ logic ตามระบบ
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // หรือปรับ logic ตามระบบ
    }

//    public Contact username(String username) {
//        return null;
//    }

    // ✅ Manual Builder class
    public static class Builder {
        private String username;
        private String email;
        private String password;
        private List<String> roles;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRoles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            return new User(username, email, password, roles);
        }
    }
}

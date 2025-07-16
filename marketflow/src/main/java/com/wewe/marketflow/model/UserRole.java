package com.wewe.marketflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_roles")
public class UserRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name; // e.g., ROLE_ADMIN, ROLE_USER

    @Column(length = 255)
    private String description;

    @Override
    public String getAuthority() {
        return name;
    }

    // Optional: Convenience constructor
    public UserRole(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Optional: for debugging/logging
    @Override
    public String toString() {
        return "UserRole{id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }
}

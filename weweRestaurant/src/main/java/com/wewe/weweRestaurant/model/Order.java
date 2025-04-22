package com.wewe.weweRestaurant.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private String tableNumber;
    private String status; // เช่น PENDING, COOKING, DONE
    private LocalDateTime createdAt;
}


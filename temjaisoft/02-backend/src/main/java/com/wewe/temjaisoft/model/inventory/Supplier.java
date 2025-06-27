package com.wewe.temjaisoft.model.inventory;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String contactPerson;

    private String phone;

    private String email;

    private String address;

    @OneToMany(mappedBy = "supplier")
    private List<Product> products;
}


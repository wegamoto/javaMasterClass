package com.wewe.temjaisoft.model.inventory;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private String location;

    // สินค้าที่สต๊อกอยู่
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String lotNumber;

    private String remark;
}


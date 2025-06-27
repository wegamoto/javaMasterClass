package com.proman.proman_erp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bill_of_materials")
@Data
public class BillOfMaterials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // ผลิตภัณฑ์หลัก

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Product material; // วัตถุดิบ

    private Integer quantityPerUnit; // จำนวนที่ใช้ต่อ 1 หน่วยของ product
}


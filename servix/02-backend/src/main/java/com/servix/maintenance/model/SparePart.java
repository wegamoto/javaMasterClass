package com.servix.maintenance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SparePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "กรุณากรอกชื่อ")
    private String name;

    @NotBlank(message = "กรุณากรอก Part Number")
    private String partNumber;

    @Min(value = 0, message = "จำนวนในสต็อกต้องไม่ติดลบ")
    private int quantityInStock;

    @Min(value = 0, message = "ระดับเตือนต้องไม่ติดลบ")
    private int reorderLevel;
}


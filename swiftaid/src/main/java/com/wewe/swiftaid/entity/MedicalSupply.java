package com.wewe.swiftaid.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class MedicalSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;             // ชื่อยา/เวชภัณฑ์
    private String description;
    private String type;             // ประเภท เช่น ยา, อุปกรณ์, PPE
    private int quantity;            // จำนวนที่มี
    private String unit;             // หน่วย เช่น กล่อง, เม็ด, ขวด
    private LocalDate expiryDate;    // วันหมดอายุ

    @ManyToOne
    private AmbulanceTeam assignedTeam; // ถ้า null = อยู่ในคลังกลาง
}



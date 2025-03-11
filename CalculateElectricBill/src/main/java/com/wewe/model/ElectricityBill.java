package com.wewe.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "electricity_bills")
@Getter
@Setter
//@NoArgsConstructor  // ใช้ Lombok สร้าง constructor แบบไม่มีพารามิเตอร์
@AllArgsConstructor // ใช้ Lombok สร้าง constructor แบบมีพารามิเตอร์
@Builder
public class ElectricityBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double units;      // จำนวนหน่วยที่ใช้ (kWh)
    private double baseCost;   // ค่าพลังงานไฟฟ้า
    private double ftCharge;   // ค่าไฟแปรผัน (Ft)
    private double serviceCharge; // ค่าบริการรายเดือน
    private double totalCost;  // ค่าไฟฟ้ารวมทั้งหมด
    private LocalDate billDate; // วันที่บิล

    // Default Constructor
    public ElectricityBill() {}

    // Constructor with parameters
    public ElectricityBill(Long id, double units, double totalCost, LocalDate billDate) {
        this.id = id;
        this.units = units;
        this.totalCost = totalCost;
        this.billDate = billDate;
    }

    public static ElectricityBill fromUnits(double units) {
        double baseCost = calculateBaseCost(units);
        double ftCharge = units * 0.25; // ค่า Ft สมมติ 0.25 บาทต่อหน่วย
        double serviceCharge = 38.22;   // ค่าบริการรายเดือน
        double totalCost = baseCost + ftCharge + serviceCharge;

        return ElectricityBill.builder()
                .units(units)
                .baseCost(baseCost)
                .ftCharge(ftCharge)
                .serviceCharge(serviceCharge)
                .totalCost(totalCost)
                .build();
    }

    public void calculateTotalCost() {
        // ค่าบริการต่อหน่วยไฟฟ้า
        double baseCostPerUnit = 3.50;  // บาท/หน่วย (อาจมีการปรับเปลี่ยนตามอัตราจริง)
        double serviceCharge = 38.22;   // ค่าบริการรายเดือน

        // คำนวณค่าไฟฟ้ารวม (units * baseCostPerUnit) + ค่าบริการ
        this.totalCost = (this.units * baseCostPerUnit) + serviceCharge;
    }

    private static double calculateBaseCost(double units) {
        if (units <= 150) {
            return units * 3.50;
        } else if (units <= 400) {
            return (150 * 3.50) + ((units - 150) * 4.20);
        } else {
            return (150 * 3.50) + (250 * 4.20) + ((units - 400) * 5.50);
        }
    }
}


package com.wewe.restaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Financial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // รหัสการเงิน

    private double revenue; // รายรับ (รายได้จากการขาย)
    private double expenses; // ค่าใช้จ่าย (ค่าใช้จ่ายต่างๆ)
    private double profitOrLoss; // กำไรหรือขาดทุน

    private String description; // รายละเอียดการเงิน เช่น "รายรับจากการขาย" หรือ "ค่าใช้จ่ายในการซื้อวัตถุดิบ"

    private String date; // วันที่บันทึกการเงิน (สามารถใช้ LocalDate หรือ Date ได้)

    public Financial() {
    }

    public Financial(double revenue, double expenses, double profitOrLoss, String description, String date) {
        this.revenue = revenue;
        this.expenses = expenses;
        this.profitOrLoss = profitOrLoss;
        this.description = description;
        this.date = date;
    }

    // คำนวณกำไรหรือขาดทุน
    public void calculateProfitOrLoss() {
        this.profitOrLoss = this.revenue - this.expenses;
    }
}


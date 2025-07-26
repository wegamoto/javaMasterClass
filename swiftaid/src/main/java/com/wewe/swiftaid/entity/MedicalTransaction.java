package com.wewe.swiftaid.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class MedicalTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MedicalSupply supply;

    @ManyToOne
    private AmbulanceTeam team;       // ทีมที่เบิกหรือรับจ่าย

    private int quantity;             // จำนวนที่เบิก/เติม
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionType type;     // ISSUE (จ่ายออก) หรือ RESTOCK (เติมเข้า)

    private String note;
}


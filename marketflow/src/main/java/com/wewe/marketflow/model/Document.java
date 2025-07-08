package com.wewe.marketflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Document {
    @Id
    @GeneratedValue
    private Long id;

    private String fileName;
    private String fileType;
    private String fileUrl; // หรือ path ในระบบไฟล์

    @ManyToOne
    private Campaign campaign;

    private LocalDate uploadedAt;
}


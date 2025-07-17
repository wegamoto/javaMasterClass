package com.wewe.springlance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    private boolean isRead; // ✅ ชื่อ field ต้องเป็นแบบนี้

    @ManyToOne
    private User receiver;

    @ManyToOne
    private ProjectRequest project;

    private String content;

    private LocalDateTime sentAt;

    // getter/setter
}


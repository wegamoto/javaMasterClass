package com.wewe.eduexam.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "options") // ✅ เปลี่ยนชื่อ table ให้ไม่ใช่ keyword
@Setter
@Getter
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean correct; // เพิ่ม field correct เพื่อใช้เก็บว่าข้อเลือกนี้ถูกต้องหรือไม่

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Option() {
    }

    public Option(String content, boolean correct, Question question) {
        this.content = content;
        this.correct = correct;
        this.question = question;
    }

    // ถ้าต้องการ override toString หลีกเลี่ยงโหลด lazy relationship
    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", correct=" + correct +
                '}';
    }

}

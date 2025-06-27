package com.wewe.eduexam.dto;

import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String studentId;
    private String studentName;
    private Set<String> role; // เพิ่มฟิลด์นี้เพื่อรับ role จาก frontend (เช่น "STUDENT", "ADMIN")
    private String confirmPassword;
    // ถ้ามีฟิลด์อื่น ๆ เช่น email หรือ fullname ก็เพิ่มได้ตามต้องการ
}


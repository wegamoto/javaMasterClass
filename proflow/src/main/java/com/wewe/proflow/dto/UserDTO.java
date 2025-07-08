package com.wewe.proflow.dto;

import com.wewe.proflow.model.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role; // เปลี่ยนจาก String → Enum
}


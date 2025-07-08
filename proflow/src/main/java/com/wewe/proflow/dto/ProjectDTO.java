package com.wewe.proflow.dto;

import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private Double budget;
    private Long ownerId; // สำหรับเชื่อม User
    private OwnerDTO owner; // เปลี่ยนจาก ownerId เป็น owner object
    private boolean active;
}


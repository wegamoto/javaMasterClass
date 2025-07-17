package com.wewe.springlance.model.enums;

public enum RoleName {
    ROLE_ADMIN,
    ROLE_CLIENT,
    ROLE_FREELANCER;

    @Override
    public String toString() {
        // แปลงเป็นชื่อที่อ่านง่าย เช่น "Admin", "Client", "Freelancer"
        return name().replace("ROLE_", "").charAt(0) + name().substring(5).toLowerCase();
    }
}

package com.wewe.marketflow.model;

/**
 * Represents the status of a marketing task.
 */
public enum TaskStatus {
    TODO,          // งานที่ยังไม่ได้เริ่ม
    IN_PROGRESS,   // งานที่กำลังดำเนินการอยู่
    DONE,          // งานที่เสร็จสมบูรณ์แล้ว
    BLOCKED,       // งานที่ถูกขัดขวาง ไม่สามารถดำเนินต่อได้
    PENDING;       // งานที่รอการดำเนินการหรือรออนุมัติ

    /**
     * Converts a string to the corresponding TaskStatus enum.
     * Case-insensitive.
     * @param status the string representation of the status
     * @return the matching TaskStatus enum
     * @throws IllegalArgumentException if no matching enum is found
     */
    public static TaskStatus fromString(String status) {
        for (TaskStatus ts : TaskStatus.values()) {
            if (ts.name().equalsIgnoreCase(status)) {
                return ts;
            }
        }
        throw new IllegalArgumentException("Unknown TaskStatus: " + status);
    }

    /**
     * Returns the enum name as a pretty string (capitalized with spaces).
     */
    @Override
    public String toString() {
        return name().replace('_', ' ').toLowerCase()
                .replaceFirst(".", String.valueOf(name().charAt(0)).toUpperCase());
    }
}

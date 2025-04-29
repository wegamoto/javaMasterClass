package com.wewe.weweShop.model.enums;

public enum OrderStatus {
    PENDING_PAYMENT,   // รอชำระเงิน
    PAID,              // ชำระเงินแล้ว
    CANCELLED,         // ยกเลิก
    REFUNDED           // คืนเงิน (Optional เผื่ออนาคต)
}

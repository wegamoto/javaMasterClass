package com.wewe.weweShop.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String user; // email หรือ username ของผู้ซื้อ

    private LocalDateTime orderDate;

    private String customerName; // ชื่อผู้ซื้อ

    private String customerPhone; // เบอร์โทร

    private String customerEmail; // Email สำหรับติดต่อ

    public enum Status {
        CREATED,
        WAITING_FOR_PAYMENT,
        PENDING_VERIFICATION,
        PENDING_PAYMENT,
        PAID,
        COMPLETED,
        CANCELLED
    }

    @Enumerated(EnumType.STRING)
    private Status status;  //  PENDING_PAYMENT, PAID, COMPLETED, CANCELLED, REFUNDED

    private BigDecimal total; // ราคารวมทั้งหมด (รวมภาษี, ค่าส่ง ขึ้นกับระบบ)

    private LocalDateTime createdAt; // เวลาการสร้างออเดอร์

    @Column(name = "payment_method")
    private String paymentMethod;

    private LocalDateTime updatedAt; // เวลาอัปเดตล่าสุด

    // กำหนด totalAmount สำหรับคำสั่งซื้อ
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#,##0.00")
    private BigDecimal totalAmount; // ยอดรวามของสินค้า (ไม่รวมภาษี, ส่วนลด, ค่าส่ง)

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public void setCreatedAt(LocalDateTime now) {
    }

    public void setUser(String user) {

    }

    public void setTotal(BigDecimal totalAmount) {
    }
}


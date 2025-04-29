CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    payment_method VARCHAR(30) NOT NULL, -- เช่น PROMPTPAY, BANK_TRANSFER
    amount_paid DECIMAL(10,2) NOT NULL, -- จำนวนเงินที่ชำระ
    payment_ref VARCHAR(100) NOT NULL, -- หมายเลขอ้างอิง เช่น เบอร์พร้อมเพย์, Ref1
    paid_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- วันที่ชำระเงิน
    slip_image_url VARCHAR(255), -- URL รูปสลิป (optional)
    
    -- กำหนด Foreign Key ไปที่ตาราง orders
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

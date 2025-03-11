-- สร้างฐานข้อมูล electricity_db
CREATE DATABASE electricity_db;

-- ใช้ฐานข้อมูล electricity_db
USE electricity_db;

-- สร้างตาราง electricity_bills
CREATE TABLE electricity_bills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- รหัสบิล (ID) เป็น AUTO_INCREMENT
    units DOUBLE NOT NULL,                     -- หน่วยการใช้ไฟฟ้า
    service_charge DOUBLE DEFAULT 38.22,       -- ค่าบริการ (ค่าบริการรายเดือน)
    total_cost DOUBLE,                         -- ค่าบริการรวม
    bill_date DATE NOT NULL,                   -- วันที่บิล
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- วันที่สร้างข้อมูล
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- วันที่แก้ไขข้อมูลล่าสุด
);

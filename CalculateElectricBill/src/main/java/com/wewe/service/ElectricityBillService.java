package com.wewe.service;

import com.wewe.model.ElectricityBill;
import com.wewe.repository.ElectricityBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ElectricityBillService {

    @Autowired
    private ElectricityBillRepository billRepository;

    // ฟังก์ชันค้นหาค่าไฟฟ้าตามช่วงเวลาที่กำหนด
    public List<ElectricityBill> getBillsByDateRange(LocalDate startDate, LocalDate endDate) {
        return billRepository.findByBillDateBetween(startDate, endDate);
    }

    // ฟังก์ชันบันทึกข้อมูลบิลไฟฟ้า
    public ElectricityBill saveBill(ElectricityBill bill) {
        // คำนวณค่าไฟฟ้าก่อนบันทึก
        bill.calculateTotalCost();
        return billRepository.save(bill);
    }

    // ฟังก์ชันแก้ไขข้อมูลบิลไฟฟ้า
    public ElectricityBill updateBill(Long billId, double units) {
        // ค้นหาบิลที่ต้องการอัพเดต
        ElectricityBill existingBill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        // อัพเดตหน่วยไฟฟ้า
        existingBill.setUnits(units);

        // คำนวณค่าไฟฟ้าหลังจากอัพเดตหน่วย
        existingBill.calculateTotalCost();

        // บันทึกการเปลี่ยนแปลง
        return billRepository.save(existingBill);
    }

    // ฟังก์ชันลบบิลไฟฟ้า
    public void deleteBill(Long billId) {
        // ตรวจสอบว่าบิลนั้นมีอยู่หรือไม่
        if (billRepository.existsById(billId)) {
            billRepository.deleteById(billId);
        } else {
            throw new RuntimeException("Bill not found");
        }
    }

    // ฟังก์ชันคำนวณค่าไฟฟ้ารวมทั้งหมดในช่วงเวลาที่กำหนด
    public double calculateTotalCostInRange(LocalDate startDate, LocalDate endDate) {
        List<ElectricityBill> bills = getBillsByDateRange(startDate, endDate);
        double totalCost = 0.0;

        for (ElectricityBill bill : bills) {
            totalCost += bill.getTotalCost();
        }

        return totalCost;
    }

    // ฟังก์ชันคำนวณค่าไฟฟ้าและบันทึกบิล
    public ElectricityBill calculateAndSaveBill(ElectricityBill bill) {
        // คำนวณค่าไฟฟ้ารวม
        bill.calculateTotalCost();

        // บันทึกบิลไฟฟ้า
        return billRepository.save(bill);
    }

    /**
     * คำนวณค่าไฟฟ้าโดยอัตโนมัติ (สามารถเพิ่มฟังก์ชันอื่นๆ ได้หากต้องการ)
     */
    public double calculateElectricityCost(double units) {
        // ค่าบริการและค่าไฟต่อหน่วย (สามารถกำหนดให้เป็นค่าคงที่ หรือดึงจากฐานข้อมูล)
        double ratePerUnit = 3.50;  // บาทต่อหน่วย
        double serviceCharge = 38.22;  // ค่าบริการ

        // คำนวณค่าไฟ
        return (units * ratePerUnit) + serviceCharge;
    }
}


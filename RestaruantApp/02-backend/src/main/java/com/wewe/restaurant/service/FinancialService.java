package com.wewe.restaurant.service;

import com.wewe.restaurant.model.Financial;
import com.wewe.restaurant.repository.FinancialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinancialService {

    @Autowired
    private FinancialRepository financialRepository;

    // เพิ่มข้อมูลการเงินใหม่
    public Financial addFinancialRecord(Financial financial) {
        financial.calculateProfitOrLoss();  // คำนวณกำไรขาดทุนก่อนบันทึก
        return financialRepository.save(financial);
    }

    // อัปเดตข้อมูลการเงิน
    public Financial updateFinancialRecord(Long id, Financial updatedFinancial) {
        Financial existingFinancial = financialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Financial record not found"));

        existingFinancial.setRevenue(updatedFinancial.getRevenue());
        existingFinancial.setExpenses(updatedFinancial.getExpenses());
        existingFinancial.setDescription(updatedFinancial.getDescription());
        existingFinancial.setDate(updatedFinancial.getDate());

        existingFinancial.calculateProfitOrLoss();  // คำนวณกำไรขาดทุนใหม่

        return financialRepository.save(existingFinancial);
    }

    // ค้นหาข้อมูลการเงินตาม ID
    public Financial getFinancialRecord(Long id) {
        return financialRepository.findById(id).orElseThrow(() -> new RuntimeException("Financial record not found"));
    }

    // ลบข้อมูลการเงิน
    public void deleteFinancialRecord(Long id) {
        financialRepository.deleteById(id);
    }
}


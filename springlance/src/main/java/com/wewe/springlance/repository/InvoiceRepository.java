package com.wewe.springlance.repository;

import com.wewe.springlance.model.Invoice;
import com.wewe.springlance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByProjectId(Long projectId);

    // ✅ นับใบแจ้งหนี้ทั้งหมดที่ยังไม่จ่าย ของลูกค้าที่เป็นเจ้าของ ProjectRequest
    int countByProjectClientAndIsPaidFalse(User client);

    List<Invoice> findByProject_Client(User client);
    List<Invoice> findByProject_Freelancer(User freelancer);
}

package com.wewe.springlance.repository;

import com.wewe.springlance.model.Invoice;
import com.wewe.springlance.model.User;
import com.wewe.springlance.model.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByProjectId(Long projectId);

    // ✅ นับใบแจ้งหนี้ทั้งหมดที่ยังไม่จ่าย ของลูกค้าที่เป็นเจ้าของ ProjectRequest
    int countByProjectClientAndIsPaidFalse(User client);

    List<Invoice> findByProject_Client(User client);
    List<Invoice> findByProject_Freelancer(User freelancer);

    long countByFreelancerAndStatus(Optional<User> freelancer, InvoiceStatus status);
}

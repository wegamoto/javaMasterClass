package com.wewe.proflow.repository;

import com.wewe.proflow.model.CashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository  // ใส่หรือไม่ใส่ก็ได้ แต่ใส่เพื่อความชัดเจน
public interface CashFlowRepository extends JpaRepository<CashFlow, Long> {

    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashFlow c WHERE c.date BETWEEN :start AND :end")
    double sumAmountForCurrentMonth(@Param("start") LocalDate start, @Param("end") LocalDate end);


    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashFlow c WHERE c.status = 'PENDING'")
    double sumPendingPayments();

}

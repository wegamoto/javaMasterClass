package com.wewe.taxcertify.repository;

import com.wewe.taxcertify.model.WithholdingTaxRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface WithholdingTaxRecordRepository extends JpaRepository<WithholdingTaxRecord, Long> {

    List<WithholdingTaxRecord> findTop5ByOrderByDateDesc();

    @Query("SELECT SUM(w.taxAmount) FROM WithholdingTaxRecord w")
    BigDecimal sumTotalTax();
}


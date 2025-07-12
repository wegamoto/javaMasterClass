package com.wewe.marketflow.repository;

import com.wewe.marketflow.model.BudgetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {
    List<BudgetItem> findByCampaignId(Long campaignId);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM BudgetItem b")
    BigDecimal totalSpent();

}

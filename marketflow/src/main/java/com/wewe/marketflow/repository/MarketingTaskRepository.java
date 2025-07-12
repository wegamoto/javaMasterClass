package com.wewe.marketflow.repository;

import com.wewe.marketflow.model.MarketingTask;
import com.wewe.marketflow.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketingTaskRepository extends JpaRepository<MarketingTask, Long> {
    List<MarketingTask> findByCampaignId(Long campaignId);
    long countByStatus(TaskStatus status);
}

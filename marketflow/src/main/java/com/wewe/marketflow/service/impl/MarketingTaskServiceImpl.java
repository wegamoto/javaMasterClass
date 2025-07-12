package com.wewe.marketflow.service.impl;

import com.wewe.marketflow.model.MarketingTask;
import com.wewe.marketflow.repository.MarketingTaskRepository;
import com.wewe.marketflow.service.MarketingTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketingTaskServiceImpl implements MarketingTaskService {

    private final MarketingTaskRepository taskRepository;

    @Override
    public List<MarketingTask> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<MarketingTask> findByCampaignId(Long campaignId) {
        return taskRepository.findByCampaignId(campaignId);
    }

    @Override
    public MarketingTask save(MarketingTask task) {
        return taskRepository.save(task);
    }

    @Override
    public MarketingTask getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}

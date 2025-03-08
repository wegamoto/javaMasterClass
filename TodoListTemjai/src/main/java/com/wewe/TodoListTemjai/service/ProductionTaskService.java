package com.wewe.TodoListTemjai.service;

import com.wewe.TodoListTemjai.entity.ProductionTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimerTask;

@Service
public class ProductionTaskService {
    @Autowired
    private ProductionTaskRepository repository;
    private Timer notificationTimer = new Timer(true);

    public ProductionTaskService() {
        startNotificationSystem();
    }

    public ProductionTask addTask(ProductionTask task) {
        return repository.save(task);
    }

    public List<ProductionTask> getPendingTasks() {
        return repository.findAll().stream().filter(task -> !task.isCompleted()).toList();
    }

    public List<ProductionTask> getCompletedTasks() {
        return repository.findAll().stream().filter(ProductionTask::isCompleted).toList();
    }

    public void markTaskAsCompleted(Long id) {
        repository.findById(id).ifPresent(task -> {
            task.markAsCompleted();
            repository.save(task);
        });
    }

    private void startNotificationSystem() {
        notificationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndNotifyTasks();
            }
        }, 0, 60000);
    }

    private void checkAndNotifyTasks() {
        LocalDateTime now = LocalDateTime.now();
        for (ProductionTask task : repository.findAll()) {
            if (!task.isCompleted() && task.hasNotification() && task.getPlannedStartTime().minusMinutes(10).isBefore(now)) {
                System.out.println("[NOTIFICATION] Task '" + task.getTaskName() + "' is starting soon at " + task.getPlannedStartTime());
            }
        }
    }
}

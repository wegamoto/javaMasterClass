package com.wewe.TodoListTemjai.entity;

import com.wewe.TodoListTemjai.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Entity
public class ProductionTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskName;
    private LocalDateTime plannedStartTime;
    private LocalDateTime plannedEndTime;
    private boolean isCompleted;
    private int priority;
    private boolean hasNotification;

    public ProductionTask() {}

    public ProductionTask(String taskName, LocalDateTime plannedStartTime, LocalDateTime plannedEndTime, int priority, boolean hasNotification) {
        this.taskName = taskName;
        this.plannedStartTime = plannedStartTime;
        this.plannedEndTime = plannedEndTime;
        this.isCompleted = false;
        this.priority = priority;
        this.hasNotification = hasNotification;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    // Getters and Setters

    @Repository
    interface ProductionTaskRepository extends JpaRepository<ProductionTask, Long> {}
}

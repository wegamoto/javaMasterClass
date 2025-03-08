package com.wewe.TodoListTemjai.controller;

import com.wewe.TodoListTemjai.entity.ProductionTask;
import com.wewe.TodoListTemjai.service.ProductionTaskService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class ProductionTaskController {
    @Autowired
    private ProductionTaskService service;

    @PostMapping
    public ProductionTask addTask(@RequestBody ProductionTask task) {
        return service.addTask(task);
    }

    @GetMapping("/pending")
    public List<ProductionTask> getPendingTasks() {
        return service.getPendingTasks();
    }

    @GetMapping("/completed")
    public List<ProductionTask> getCompletedTasks() {
        return service.getCompletedTasks();
    }

    @PutMapping("/complete/{id}")
    public void markTaskAsCompleted(@PathVariable Long id) {
        service.markTaskAsCompleted(id);
    }
}

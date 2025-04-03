package com.wewe.sqlitetodolist.service;

import com.wewe.sqlitetodolist.model.Task;
import com.wewe.sqlitetodolist.model.TaskDAO;

import java.util.List;

public class TaskService {
    private TaskDAO taskDAO;

    public TaskService(String dbPath) {
        this.taskDAO = new TaskDAO(dbPath);
    }

    public void addTask(String title, String description) {
        Task task = new Task(0, title, description, false);
        taskDAO.addTask(task);
    }

    public List<Task> getTasks() {
        return taskDAO.getAllTasks();
    }

    public void markTaskAsCompleted(int id) {
        taskDAO.updateTask(id, true);
    }

    public void deleteTask(int id) {
        taskDAO.deleteTask(id);
    }
}


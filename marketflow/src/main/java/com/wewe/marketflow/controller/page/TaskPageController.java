package com.wewe.marketflow.controller.page;

import com.wewe.marketflow.model.MarketingTask;
import com.wewe.marketflow.model.TaskStatus;
import com.wewe.marketflow.service.MarketingTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskPageController {

    private final MarketingTaskService marketingTaskService;

    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", marketingTaskService.getAll());
        return "tasks/list"; // src/main/resources/templates/task/list.html
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("task", new MarketingTask());
        model.addAttribute("statuses", TaskStatus.values());
        return "tasks/form"; // src/main/resources/templates/task/form.html
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        MarketingTask task = marketingTaskService.getById(id);
        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());
        return "tasks/form";
    }

    @PostMapping("/save")
    public String saveTask(@ModelAttribute("task") MarketingTask task) {
        marketingTaskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        marketingTaskService.delete(id);
        return "redirect:/tasks";
    }
}


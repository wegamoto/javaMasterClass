package com.servix.maintenance.controller;

import com.servix.maintenance.model.WorkOrder;
import com.servix.maintenance.model.WorkOrderStatus;
import com.servix.maintenance.repository.WorkOrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/maintenancelogs")
public class MaintenanceLogController {

    private final WorkOrderRepository workOrderRepository;

    public MaintenanceLogController(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @GetMapping
    public String showCompletedLogs(Model model) {
        List<WorkOrder> completedLogs = workOrderRepository.findByStatus(WorkOrderStatus.COMPLETED);
        model.addAttribute("logs", completedLogs);
        return "maintenancelogs/list";
    }
}


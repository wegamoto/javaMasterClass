package com.proman.proman_erp.controller;

import com.proman.proman_erp.dto.WorkOrderDTO;
import com.proman.proman_erp.entity.WorkOrder;
import com.proman.proman_erp.service.WorkOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {
    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping
    public List<WorkOrderDTO> getAll() {
        return workOrderService.getAll();
    }

    @PostMapping
    public WorkOrderDTO create(@RequestBody WorkOrderDTO dto) {
        return workOrderService.save(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        workOrderService.deleteById(id);
    }
}

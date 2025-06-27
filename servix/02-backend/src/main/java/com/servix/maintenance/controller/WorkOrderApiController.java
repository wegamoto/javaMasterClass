package com.servix.maintenance.controller;

import com.servix.maintenance.dto.WorkOrderDTO;
import com.servix.maintenance.dto.WorkOrderCreateDTO;
import com.servix.maintenance.dto.WorkOrderUpdateDTO;
import com.servix.maintenance.service.WorkOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderApiController {

    private final WorkOrderService workOrderService;

    @GetMapping
    public List<WorkOrderDTO> getAll() {
        return workOrderService.getAllWorkOrders();
    }

    @GetMapping("/{id}")
    public WorkOrderDTO getById(@PathVariable Long id) {
        return workOrderService.getWorkOrderDtoById(id);
    }

    @PostMapping
    public WorkOrderDTO create(@Valid @RequestBody WorkOrderCreateDTO createDTO) {
        return workOrderService.createWorkOrder(createDTO);
    }

    @PutMapping("/{id}")
    public WorkOrderDTO update(@PathVariable Long id, @RequestBody WorkOrderUpdateDTO updateDTO) {
        return workOrderService.updateWorkOrder(id, updateDTO);
    }
}

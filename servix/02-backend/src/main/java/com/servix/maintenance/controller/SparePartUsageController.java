package com.servix.maintenance.controller;

import com.servix.maintenance.dto.SparePartUsageDTO;
import com.servix.maintenance.model.SparePartUsage;
import com.servix.maintenance.service.SparePartUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spare-usage")
@RequiredArgsConstructor
public class SparePartUsageController {

    private final SparePartUsageService usageService;

    @PostMapping("/assign")
    public SparePartUsageDTO assignPartToWorkOrder(
            @RequestParam Long workOrderId,
            @RequestParam Long sparePartId,
            @RequestParam int quantity
    ) {
        return usageService.assignPartToWorkOrder(workOrderId, sparePartId, quantity);
    }
}

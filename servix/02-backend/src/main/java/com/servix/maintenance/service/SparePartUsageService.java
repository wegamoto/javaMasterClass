package com.servix.maintenance.service;

import com.servix.maintenance.dto.SparePartUsageDTO;
import com.servix.maintenance.mapper.SparePartUsageMapper;
import com.servix.maintenance.model.SparePart;
import com.servix.maintenance.model.SparePartUsage;
import com.servix.maintenance.model.WorkOrder;
import com.servix.maintenance.repository.SparePartUsageRepository;
import com.servix.maintenance.repository.WorkOrderRepository;
import com.servix.maintenance.repository.SparePartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SparePartUsageService {

    private final SparePartUsageRepository usageRepository;
    private final WorkOrderRepository workOrderRepository;
    private final SparePartRepository sparePartRepository;
    private final SparePartUsageMapper sparePartUsageMapper;

    public List<SparePartUsage> getAllUsage() {
        return usageRepository.findAll();
    }

    public SparePartUsage createUsage(SparePartUsage usage) {
        return usageRepository.save(usage);
    }

    /**
     * รวม logic ลด stock + สร้าง SparePartUsage + ผูกกับ WorkOrder
     */
    public SparePartUsageDTO assignPartToWorkOrder(Long workOrderId, Long sparePartId, int quantityUsed) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("WorkOrder not found"));

        SparePart sparePart = sparePartRepository.findById(sparePartId)
                .orElseThrow(() -> new RuntimeException("SparePart not found"));

        if (sparePart.getQuantityInStock() < quantityUsed) {
            throw new RuntimeException("Insufficient stock");
        }

        sparePart.setQuantityInStock(sparePart.getQuantityInStock() - quantityUsed);
        sparePartRepository.save(sparePart);

        SparePartUsage usage = SparePartUsage.builder()
                .sparePart(sparePart)
                .workOrder(workOrder)
                .quantityUsed(quantityUsed)
                .build();

        usage = usageRepository.save(usage);
        return sparePartUsageMapper.toDto(usage);
    }

}

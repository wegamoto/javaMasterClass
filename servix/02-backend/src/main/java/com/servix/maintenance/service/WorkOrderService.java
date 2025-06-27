package com.servix.maintenance.service;

import com.servix.maintenance.dto.WorkOrderCreateDTO;
import com.servix.maintenance.dto.WorkOrderDTO;
import com.servix.maintenance.dto.WorkOrderUpdateDTO;
import com.servix.maintenance.exception.ResourceNotFoundException;
import com.servix.maintenance.mapper.WorkOrderMapper;
import com.servix.maintenance.model.WorkOrder;
import com.servix.maintenance.model.WorkOrderStatus;
import com.servix.maintenance.repository.UserRepository;
import com.servix.maintenance.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkOrderService implements IWorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderMapper workOrderMapper;
    private final UserRepository userRepository;

    @Override
    public List<WorkOrderDTO> getAllWorkOrders() {
        return workOrderRepository.findAll().stream()
                .map(workOrderMapper::toDto)
                .toList();
    }

    @Override
    public WorkOrderDTO getWorkOrderDtoById(Long id) {
        return workOrderMapper.toDto(getWorkOrderById(id));
    }

    @Override
    public WorkOrderDTO createWorkOrder(WorkOrderCreateDTO dto) {
        WorkOrder order = new WorkOrder();
        order.setDescription(dto.getDescription());
        // ใช้ค่า default จาก enum
        order.setStatus(dto.getStatus() != null ? dto.getStatus() : WorkOrderStatus.OPEN);
        workOrderRepository.save(order);
        return workOrderMapper.toDto(order);
    }

    @Override
    public WorkOrderDTO updateWorkOrder(Long id, WorkOrderUpdateDTO dto) {
        WorkOrder workOrder = getWorkOrderById(id);

        // อัปเดตเฉพาะ field ที่ส่งมา (ไม่ null)
        Optional.ofNullable(dto.getDescription()).ifPresent(workOrder::setDescription);
        Optional.ofNullable(dto.getStatus()).ifPresent(workOrder::setStatus);
        Optional.ofNullable(dto.getCompletedAt()).ifPresent(workOrder::setCompletedAt);

        workOrderRepository.save(workOrder);
        return workOrderMapper.toDto(workOrder);
    }

    public WorkOrder getWorkOrderById(Long id) {
        return workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work order not found with id: " + id));
    }

    public void save(WorkOrder workOrder) {
        workOrderRepository.save(workOrder);
    }

    public WorkOrderDTO convertToDTO(WorkOrder workOrder) {
        WorkOrderDTO dto = new WorkOrderDTO();
        dto.setId(workOrder.getId());
        dto.setDescription(workOrder.getDescription());
        dto.setStatus(workOrder.getStatus().name());
        dto.setCreatedAt(workOrder.getCreatedAt());
        dto.setCompletedAt(workOrder.getCompletedAt());

        // ✅ ต้องมีการเซ็ตชื่ออุปกรณ์
        if (workOrder.getEquipmentName() != null) {
            dto.setEquipmentName(workOrder.getEquipmentName().toString());
        }

        return dto;
    }

    @Override
    public long countAll() {
        return workOrderRepository.count();
    }

    @Override
    public long countInProgress() {
        return workOrderRepository.countByStatus(WorkOrderStatus.IN_PROGRESS);
    }

    @Override
    public long countCompleted() {
        return workOrderRepository.countByStatus(WorkOrderStatus.COMPLETED);
    }

    @Override
    public long countTechnicians() {
        return userRepository.countByRoleName("TECHNICIAN");
    }

    @Override
    public List<WorkOrderDTO> findRecentWorkOrders() {
        return workOrderRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(workOrderMapper::toDto)
                .toList();
    }

}

package com.servix.maintenance.service;

import com.servix.maintenance.dto.WorkOrderCreateDTO;
import com.servix.maintenance.dto.WorkOrderDTO;
import com.servix.maintenance.dto.WorkOrderUpdateDTO;

import java.util.List;

public interface IWorkOrderService {
    List<WorkOrderDTO> getAllWorkOrders();
    WorkOrderDTO getWorkOrderDtoById(Long id);
    WorkOrderDTO createWorkOrder(WorkOrderCreateDTO dto);
    WorkOrderDTO updateWorkOrder(Long id, WorkOrderUpdateDTO dto);
    long countAll();
    long countInProgress();
    long countCompleted();
    long countTechnicians();
    List<WorkOrderDTO> findRecentWorkOrders();
}



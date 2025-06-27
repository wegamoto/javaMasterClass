package com.servix.maintenance.repository;

import com.servix.maintenance.model.WorkOrder;
import com.servix.maintenance.model.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    long count(); // นี่ถูก built-in อยู่แล้วใน JpaRepository
    long countByStatus(WorkOrderStatus status);
    List<WorkOrder> findTop5ByOrderByCreatedAtDesc();
    List<WorkOrder> findByStatus(WorkOrderStatus status);

}

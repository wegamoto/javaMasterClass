package com.proman.proman_erp.repository;

import com.proman.proman_erp.entity.WorkOrder;
import com.proman.proman_erp.enums.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    List<WorkOrder> findByStatus(WorkOrderStatus status);

    @Query("SELECT w FROM WorkOrder w WHERE w.scheduledDate BETWEEN :start AND :end")
    List<WorkOrder> findByScheduledDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}

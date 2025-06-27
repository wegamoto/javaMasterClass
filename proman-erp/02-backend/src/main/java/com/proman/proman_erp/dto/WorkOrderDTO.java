package com.proman.proman_erp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkOrderDTO {
    private Long id;
    private String orderNumber;
    private Long productId;
    private String productName;
    private Integer quantity;
    private LocalDateTime scheduledDate;
    private String status;
}

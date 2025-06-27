package com.proman.proman_erp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MrpPlanDTO {
    private Long id;
    private LocalDateTime planningDate;
    private Long productId;
    private String productName;
    private Integer requiredQuantity;
    private Integer availableQuantity;
    private Integer toProduceQuantity;
}

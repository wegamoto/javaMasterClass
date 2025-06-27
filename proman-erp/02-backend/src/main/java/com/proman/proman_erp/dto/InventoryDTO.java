package com.proman.proman_erp.dto;

import lombok.Data;

@Data
public class InventoryDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantityOnHand;
    private Integer quantityReserved;
}

package com.proman.proman_erp.dto;

import lombok.Data;

@Data
public class BillOfMaterialsDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Long materialId;
    private String materialName;
    private Integer quantityPerUnit;
}

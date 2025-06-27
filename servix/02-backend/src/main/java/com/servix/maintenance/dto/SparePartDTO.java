package com.servix.maintenance.dto;

import lombok.Data;

@Data
public class SparePartDTO {
    private Long id;
    private String name;
    private int quantityInStock;
}


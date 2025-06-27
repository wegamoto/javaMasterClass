package com.proman.proman_erp.mapper;

import com.proman.proman_erp.dto.InventoryDTO;
import com.proman.proman_erp.entity.Inventory;
import com.proman.proman_erp.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {
    public InventoryDTO toDTO(Inventory entity) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setQuantityOnHand(entity.getQuantityOnHand());
        dto.setQuantityReserved(entity.getQuantityReserved());
        return dto;
    }

    public Inventory toEntity(InventoryDTO dto, Product product) {
        Inventory entity = new Inventory();
        entity.setId(dto.getId());
        entity.setProduct(product);
        entity.setQuantityOnHand(dto.getQuantityOnHand());
        entity.setQuantityReserved(dto.getQuantityReserved());
        return entity;
    }
}

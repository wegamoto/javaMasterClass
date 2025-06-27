package com.proman.proman_erp.mapper;

import com.proman.proman_erp.dto.BillOfMaterialsDTO;
import com.proman.proman_erp.entity.BillOfMaterials;
import com.proman.proman_erp.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class BillOfMaterialsMapper {
    public BillOfMaterialsDTO toDTO(BillOfMaterials entity) {
        BillOfMaterialsDTO dto = new BillOfMaterialsDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setMaterialId(entity.getMaterial().getId());
        dto.setMaterialName(entity.getMaterial().getName());
        dto.setQuantityPerUnit(entity.getQuantityPerUnit());
        return dto;
    }

    public BillOfMaterials toEntity(BillOfMaterialsDTO dto, Product product, Product material) {
        BillOfMaterials entity = new BillOfMaterials();
        entity.setId(dto.getId());
        entity.setProduct(product);
        entity.setMaterial(material);
        entity.setQuantityPerUnit(dto.getQuantityPerUnit());
        return entity;
    }
}

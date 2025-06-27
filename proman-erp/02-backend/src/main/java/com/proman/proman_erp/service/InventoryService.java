package com.proman.proman_erp.service;

import com.proman.proman_erp.dto.InventoryDTO;
import com.proman.proman_erp.entity.Inventory;
import com.proman.proman_erp.entity.Product;
import com.proman.proman_erp.mapper.InventoryMapper;
import com.proman.proman_erp.repository.InventoryRepository;
import com.proman.proman_erp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryService(InventoryRepository inventoryRepository, ProductRepository productRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.inventoryMapper = inventoryMapper;
    }

    public List<InventoryDTO> getAll() {
        return inventoryRepository.findAll().stream()
                .map(inventoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public InventoryDTO save(InventoryDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Inventory entity = inventoryMapper.toEntity(dto, product);
        return inventoryMapper.toDTO(inventoryRepository.save(entity));
    }

    public void deleteById(Long id) {
        inventoryRepository.deleteById(id);
    }
}

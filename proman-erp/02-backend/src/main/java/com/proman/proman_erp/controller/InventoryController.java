package com.proman.proman_erp.controller;

import com.proman.proman_erp.dto.InventoryDTO;
import com.proman.proman_erp.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryDTO> getAll() {
        return inventoryService.getAll();
    }

    @PostMapping
    public InventoryDTO create(@RequestBody InventoryDTO dto) {
        return inventoryService.save(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        inventoryService.deleteById(id);
    }
}

package com.servix.maintenance.service;

import com.servix.maintenance.model.SparePart;
import com.servix.maintenance.repository.SparePartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SparePartService {

    private final SparePartRepository sparePartRepository;

    public List<SparePart> getAllSpareParts() {
        return sparePartRepository.findAll();
    }

    public SparePart getSparePartById(Long id) {
        return sparePartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spare part not found"));
    }

    public SparePart createSparePart(SparePart sparePart) {
        return sparePartRepository.save(sparePart);
    }

    public SparePart updateStock(Long id, int newQuantity) {
        SparePart part = getSparePartById(id);
        part.setQuantityInStock(newQuantity);
        return sparePartRepository.save(part);
    }

    public SparePart reduceStock(Long id, int quantityUsed) {
        SparePart part = getSparePartById(id);
        if (part.getQuantityInStock() < quantityUsed) {
            throw new RuntimeException("Not enough stock for part: " + part.getName());
        }
        part.setQuantityInStock(part.getQuantityInStock() - quantityUsed);
        return sparePartRepository.save(part);
    }
}


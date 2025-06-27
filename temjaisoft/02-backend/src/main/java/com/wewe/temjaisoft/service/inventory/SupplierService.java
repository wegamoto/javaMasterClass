package com.wewe.temjaisoft.service.inventory;

import com.wewe.temjaisoft.model.inventory.Supplier;
import com.wewe.temjaisoft.repository.inventory.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepo;

    public SupplierService(SupplierRepository supplierRepo) {
        this.supplierRepo = supplierRepo;
    }

    public List<Supplier> getAll() {
        return supplierRepo.findAll();
    }

    public Optional<Supplier> getById(Long id) {
        return supplierRepo.findById(id);
    }

    public void save(Supplier supplier) {
        supplierRepo.save(supplier);
    }

    public void delete(Long id) {
        supplierRepo.deleteById(id);
    }
}


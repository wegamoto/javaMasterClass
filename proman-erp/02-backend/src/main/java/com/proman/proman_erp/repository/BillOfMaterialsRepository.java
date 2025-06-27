package com.proman.proman_erp.repository;

import com.proman.proman_erp.entity.BillOfMaterials;
import com.proman.proman_erp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillOfMaterialsRepository extends JpaRepository<BillOfMaterials, Long> {
    List<BillOfMaterials> findByProduct(Product product);
}

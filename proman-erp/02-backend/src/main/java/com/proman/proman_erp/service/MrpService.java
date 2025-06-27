package com.proman.proman_erp.service;

import com.proman.proman_erp.dto.MrpPlanDTO;
import com.proman.proman_erp.entity.BillOfMaterials;
import com.proman.proman_erp.entity.Inventory;
import com.proman.proman_erp.entity.MrpPlan;
import com.proman.proman_erp.entity.Product;
import com.proman.proman_erp.mapper.MrpPlanMapper;
import com.proman.proman_erp.repository.BillOfMaterialsRepository;
import com.proman.proman_erp.repository.MrpPlanRepository;
import com.proman.proman_erp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MrpService {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final BillOfMaterialsRepository bomRepository;
    private final MrpPlanRepository mrpPlanRepository;
    private final MrpPlanMapper mapper;

    public MrpService(ProductRepository productRepository,
                      InventoryRepository inventoryRepository,
                      BillOfMaterialsRepository bomRepository,
                      MrpPlanRepository mrpPlanRepository,
                      MrpPlanMapper mapper) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.bomRepository = bomRepository;
        this.mrpPlanRepository = mrpPlanRepository;
        this.mapper = mapper;
    }

    public List<MrpPlanDTO> generateMrpForProduct(Long productId, int requiredQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<BillOfMaterials> boms = bomRepository.findByProduct(product);
        List<MrpPlanDTO> plans = new ArrayList<>();

        for (BillOfMaterials bom : boms) {
            Product material = bom.getMaterial();
            int totalRequired = bom.getQuantityPerUnit() * requiredQuantity;
            Inventory inv = inventoryRepository.findByProduct(material)
                    .orElseGet(() -> {
                        Inventory i = new Inventory();
                        i.setProduct(material);
                        i.setQuantityOnHand(0);
                        i.setQuantityReserved(0);
                        return i;
                    });

            int available = inv.getQuantityOnHand() - inv.getQuantityReserved();
            int toProduce = Math.max(0, totalRequired - available);

            MrpPlan plan = new MrpPlan();
            plan.setPlanningDate(LocalDateTime.now());
            plan.setProduct(material);
            plan.setRequiredQuantity(totalRequired);
            plan.setAvailableQuantity(available);
            plan.setToProduceQuantity(toProduce);

            mrpPlanRepository.save(plan);
            plans.add(mapper.toDTO(plan));
        }

        return plans;
    }
}

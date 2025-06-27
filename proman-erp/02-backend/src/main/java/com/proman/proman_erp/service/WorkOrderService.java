package com.proman.proman_erp.service;

import com.proman.proman_erp.dto.WorkOrderDTO;
import com.proman.proman_erp.entity.Product;
import com.proman.proman_erp.entity.WorkOrder;
import com.proman.proman_erp.mapper.WorkOrderMapper;
import com.proman.proman_erp.repository.ProductRepository;
import com.proman.proman_erp.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkOrderService {
    private final WorkOrderRepository workOrderRepository;
    private final ProductRepository productRepository;
    private final WorkOrderMapper mapper;

    public WorkOrderService(WorkOrderRepository workOrderRepository, ProductRepository productRepository, WorkOrderMapper mapper) {
        this.workOrderRepository = workOrderRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    public List<WorkOrderDTO> getAll() {
        return workOrderRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public WorkOrderDTO save(WorkOrderDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        WorkOrder entity = mapper.toEntity(dto, product);
        return mapper.toDTO(workOrderRepository.save(entity));
    }

    public void deleteById(Long id) {
        workOrderRepository.deleteById(id);
    }
}

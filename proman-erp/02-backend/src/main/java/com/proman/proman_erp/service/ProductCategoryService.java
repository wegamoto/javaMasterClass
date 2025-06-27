package com.proman.proman_erp.service;

import com.proman.proman_erp.entity.ProductCategory;
import com.proman.proman_erp.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository categoryRepository;

    public ProductCategoryService(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<ProductCategory> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<ProductCategory> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public ProductCategory save(ProductCategory category) {
        return categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}

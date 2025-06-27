package com.wewe.temjaisoft.service.inventory;

import com.wewe.temjaisoft.model.inventory.Category;
import com.wewe.temjaisoft.repository.inventory.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 🔹 ดึงรายการหมวดหมู่ทั้งหมด
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // 🔹 ดึงข้อมูลหมวดหมู่ตาม ID (ถ้ามี)
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    // 🔹 เพิ่มหรืออัปเดตหมวดหมู่
    public void save(Category category) {
        categoryRepository.save(category);
    }

    // 🔹 ลบหมวดหมู่ตาม ID
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}

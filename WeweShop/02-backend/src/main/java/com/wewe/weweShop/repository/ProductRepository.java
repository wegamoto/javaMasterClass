package com.wewe.weweShop.repository;

import com.wewe.weweShop.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByStockLessThan(int stock);
    List<Product> findByStockLessThan(int stock);

    // ค้นหาสินค้าตามหมวดหมู่
    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p ORDER BY function('RAND')")
    List<Product> findRecommendedProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name IN :categories ORDER BY function('RAND')")
    List<Product> findRecommendedProductsByCategories(@Param("categories") List<String> categories, Pageable pageable);
}

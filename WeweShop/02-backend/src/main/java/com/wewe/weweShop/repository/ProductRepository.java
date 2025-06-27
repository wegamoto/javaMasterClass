package com.wewe.weweShop.repository;

import com.wewe.weweShop.model.Product;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByStockLessThan(int stock);
    List<Product> findByStockLessThan(int stock);

    // ค้นหาสินค้าตามหมวดหมู่
    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p ORDER BY function('RAND')")
    List<Product> findRecommendedProducts(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE product SET stock = stock + :quantity WHERE id = :productId", nativeQuery = true)
    int increaseStockNative(@Param("productId") Long productId, @Param("quantity") int quantity);

    @Query("SELECT p FROM Product p WHERE p.category.name IN :categories ORDER BY function('RAND')")
    List<Product> findRecommendedProductsByCategories(@Param("categories") List<String> categories, Pageable pageable);

    List<Product> findTop6ByOrderByCreatedAtDesc();

    // ค้นหาสินค้าที่มีชื่อคล้ายกับคำค้น (ไม่คำนึงถึงตัวพิมพ์เล็กพิมพ์ใหญ่)
    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findTop5ByOrderByStockAsc();

    List<Product> findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(String name, String categoryName);

    Optional<Product> findByProductCode(String productCode);

}

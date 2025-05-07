package com.wewe.weweShop.service;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    @Value("${upload.dir}") // ใช้จาก application.properties เช่น: upload.dir=uploads
    private String uploadDir;

    @Autowired
    private ProductRepository productRepository;

    // ✅ แสดงสินค้าทั้งหมด
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    // ✅ แสดงสินค้าทั้งหมด
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void saveProduct(Product product) {
        productRepository.save(product);  // ต้องไม่มี exception เช่น field null
    }

    // ✅ แสดงสินค้ารายตัว
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    // ✅ เพิ่มสินค้าใหม่
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // ✅ แก้ไขสินค้า
    public Product updateProduct(Long id, Product updatedProduct, MultipartFile imageFile) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));

        existingProduct = getProductById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());

        // อัปเดตภาพถ้ามีอัปโหลดใหม่
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            existingProduct.setImage(updatedProduct.getImage());
        }

        return productRepository.save(existingProduct);
    }

    // ✅ ลบสินค้า
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getRecommendations(int page) {
        Pageable pageable = PageRequest.of(page, 6); // โหลดทีละ 6 ชิ้น
        return productRepository.findRecommendedProducts(pageable);
    }

    // ดึงสินค้าตามหมวดหมู่
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getFeaturedProducts() {
        // ดึงสินค้าที่ต้องการแสดงหน้าแรก เช่น top 6 สินค้า
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    private String saveImage(MultipartFile imageFile) {
        try {
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get("uploads/" + filename);
            Files.copy(imageFile.getInputStream(), path);
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    public void saveProductWithImage(Product product, MultipartFile imageFile) throws IOException {

        // ✅ ใช้ path ภายนอกที่ชัดเจน
        String uploadDir = "C:/weweshop/uploads";

        if (imageFile != null && !imageFile.isEmpty()) {
            // ดึงชื่อไฟล์ต้นฉบับ
            String originalFileName = imageFile.getOriginalFilename();

            // ✅ ทำความสะอาดชื่อไฟล์
            assert originalFileName != null;
            String cleanFileName = originalFileName
                    .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_")
                    .replace("'", "")
                    .trim();


            // 🔒 ป้องกันชื่อซ้ำด้วย UUID
            String uuid = String.valueOf(UUID.randomUUID());
            String savedFilename = uuid + "_" + cleanFileName;

            // 📂 สร้าง path เต็มของไฟล์
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 📥 บันทึกไฟล์
            Path filePath = uploadPath.resolve(savedFilename);
            imageFile.transferTo(filePath.toFile());

            // 💾 บันทึกชื่อไฟล์ลง DB (ไม่รวม path)
            product.setImage(savedFilename);
        }

        // 💡 บันทึกข้อมูลสินค้า
        productRepository.save(product);
    }

    public List<Product> searchProducts(String searchQuery) {
        return productRepository.findByNameContainingIgnoreCase(searchQuery);  // ค้นหาสินค้าที่มีชื่อคล้ายกับคำค้น
    }

    public void increaseStock(Long productId, int quantity) {
        productRepository.findById(productId).ifPresent(product -> {
            product.setStock(product.getStock() + quantity);
            productRepository.save(product);
        });
    }


    // ลด stock อย่างปลอดภัย
    @Transactional
    public void decreaseStock(Long productId, int quantity, String reason) {
        productRepository.findById(productId).ifPresent(product -> {
            int remaining = product.getStock() - quantity;
            if (remaining < 0) {
                throw new IllegalStateException("สต็อกไม่เพียงพอ");
            }
            product.setStock(remaining);
            productRepository.save(product);
            // เพิ่ม logging หรือประวัติได้ที่นี่
            // คุณสามารถเพิ่ม log การลด stock ไว้ในตารางแยกได้ เช่น StockLog
            log.info("Stock decreased for {} (ID: {}) by {} units. Reason: {}",
                    product.getName(), product.getId(), quantity, reason);
        });
    }

    // ตรวจสอบว่าสินค้าคงเหลือพอหรือไม่
    public boolean isInStock(Long productId, int quantity) {
        return productRepository.findById(productId)
                .map(product -> product.getStock() >= quantity)
                .orElse(false);
    }

    public List<Product> getTop5LowStockProducts() {
        return productRepository.findTop5ByOrderByStockAsc();
    }

    public List<Product> searchProductsByNameOrCategory(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(keyword, keyword);
    }



}

package com.wewe.temjaiShop.service;

import com.wewe.temjaiShop.config.UploadProperties;
import com.wewe.temjaiShop.model.Category;
import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.repository.CategoryRepository;
import com.wewe.temjaiShop.repository.ProductRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {

    @Value("${upload.dir}") // ใช้จาก application.properties เช่น: upload.dir=uploads
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png");

    private final ProductRepository productRepository;
    private final UploadProperties uploadProperties;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, UploadProperties uploadProperties, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.uploadProperties = uploadProperties;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // ✅ ตรวจสอบว่า uploadDir มีอยู่จริงหรือไม่
    @PostConstruct
    public void checkUploadDir() {
        Path path = Path.of(uploadDir).toAbsolutePath().normalize();
        System.out.println("🧾 [UPLOAD_DIR]: " + path);

        if (!Files.exists(path)) {
            System.err.println("⚠️ [WARNING] Upload directory does NOT exist: " + path);
        } else {
            System.out.println("✅ Upload directory exists: " + path);
        }
    }

    private final UserRepository userRepository;


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

        // บันทึกครั้งแรกเพื่อให้ได้ id ที่ถูก generate
        Product savedProduct = productRepository.save(product);

        // สร้างรหัสสินค้าอิงจาก id (เช่น P001)
        String generatedCode = String.format("P%03d", savedProduct.getId());
        savedProduct.setProductCode(generatedCode);

        // บันทึกสินค้าอีกครั้งเพื่อให้มีการอัพเดตรหัสสินค้า
        return productRepository.save(savedProduct);  // ใช้ productRepository.save(savedProduct)
    }

    // ✅ แก้ไขสินค้า
    public Product updateProduct(Long id, Product updatedProduct, MultipartFile imageFile) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());

        // หากมีไฟล์ใหม่ ให้อัปเดต
        if (imageFile != null && !imageFile.isEmpty()) {
            // ลบไฟล์เดิม
            deleteImageIfExists(existingProduct.getImage());

            // บันทึกไฟล์ใหม่
            String newimageUrl = saveImage(imageFile);
            existingProduct.setImage(newimageUrl);
        }

        return productRepository.save(existingProduct);
    }

    private void deleteImageIfExists(String imageName) {
        if (imageName == null || imageName.isEmpty()) return;

        String os = System.getProperty("os.name").toLowerCase();
        String uploadDir;

        if (os.contains("win")) {
            uploadDir = "C:/temjaishop/uploads/";
        } else {
            uploadDir = "/home/ubuntu/temjaishop/uploads/";
        }

        File file = new File(uploadDir + imageName);
        if (file.exists()) {
            file.delete();
        }
    }



    // ✅ ลบสินค้า
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getRecommendations(int page) {
        Pageable pageable = PageRequest.of(page, 6); // โหลดทีละ 6 ชิ้น
        return productRepository.findRecommendedProducts(pageable);
    }

    // เมธอดใหม่: ดึงสินค้าแนะนำเฉพาะผู้ใช้
    public List<Product> getRecommendationsForUser(String usernameOrEmail) {
        if (usernameOrEmail == null || usernameOrEmail.isBlank()) {
            return productRepository.findTop10ByOrderBySoldCountDesc();
        }

        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(usernameOrEmail);

        if (optionalUser.isEmpty()) {
            // ลองหาด้วย email (กรณี Facebook login)
            optionalUser = userRepository.findByUsernameOrEmail(usernameOrEmail);
        }

        if (optionalUser.isEmpty()) {
            return productRepository.findTop10ByOrderBySoldCountDesc();
        }

        User user = optionalUser.get();

        String favoriteCategory = user.getFavoriteCategory();

        if (favoriteCategory != null && !favoriteCategory.isBlank()) {
            return productRepository.findTop10ByCategory_NameOrderBySoldCountDesc(favoriteCategory);
        }

        return productRepository.findTop10ByOrderBySoldCountDesc();
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

    private String saveImage(MultipartFile file) {
        String os = System.getProperty("os.name").toLowerCase();
        String uploadDir;

        if (os.contains("win")) {
            uploadDir = "C:/temjaishop/uploads/";
        } else {
            uploadDir = "/opt/temjaishop/uploads/";
        }

        // สร้างชื่อไฟล์ใหม่ไม่ให้ซ้ำกัน
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        try {
            Path path = Paths.get(uploadDir + newFilename);
            Files.createDirectories(path.getParent()); // เผื่อโฟลเดอร์ยังไม่มี
            file.transferTo(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        return newFilename;
    }

    @Transactional
    public void saveProductWithImage(Product product, MultipartFile imageFile) throws IOException {
        if (isValidImageFile(imageFile)) {
            // ลบรูปเก่า ถ้ามี
            deleteOldImageIfExists(product.getImage());

            // บันทึกรูปใหม่
            String savedFilename = storeImageFile(imageFile);
            product.setImage(savedFilename);
        }

        productRepository.save(product);
    }

    private String storeImageFile(MultipartFile imageFile) throws IOException {
        String cleanFileName = sanitizeFileName(imageFile.getOriginalFilename());
        String savedFilename = UUID.randomUUID() + "_" + cleanFileName;

        Path uploadPath = getUploadPath();
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(savedFilename);
        imageFile.transferTo(filePath.toFile());

        return savedFilename;
    }

    private void deleteOldImageIfExists(String filename) {
        if (filename == null || filename.isEmpty()) return;

        String os = System.getProperty("os.name").toLowerCase();
        String uploadDir = os.contains("win") ?
                "C:/temjaishop/uploads/" : "/opt/temjaishop/uploads/";

        Path path = Paths.get(uploadDir, filename);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // ล็อกไว้ ไม่ให้กระทบ flow หลัก
            System.err.println("Failed to delete old image: " + e.getMessage());
        }
    }

    private String sanitizeFileName(String originalFileName) {
        return originalFileName
                .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_")
                .replace("'", "")
                .trim();
    }

    private Path getUploadPath() {
        return Paths.get(uploadProperties.getDir()).toAbsolutePath().normalize();
    }

    private boolean isValidImageFile(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) return false;

        // ตรวจสอบชนิดของไฟล์
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Unsupported file type: " + file.getContentType());
        }

        // ตรวจสอบขนาดไฟล์
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum allowed size of 5MB");
        }

        return true;
    }


//    public void saveProductWithImage(Product product, MultipartFile imageFile) throws IOException {
//        if (imageFile != null && !imageFile.isEmpty()) {
//            // ✅ ทำความสะอาดชื่อไฟล์ต้นฉบับ
//            String originalFileName = imageFile.getOriginalFilename();
//            if (originalFileName == null) {
//                throw new IOException("Invalid file name");
//            }
//
//            String cleanFileName = originalFileName
//                    .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_")
//                    .replace("'", "")
//                    .trim();
//
//            // 🔒 ป้องกันชื่อซ้ำด้วย UUID
//            String savedFilename = UUID.randomUUID() + "_" + cleanFileName;
//
//            // 📂 ใช้ค่าจาก application.yml
//            Path uploadPath = Paths.get(uploadProperties.getDir()).toAbsolutePath().normalize();
//            Files.createDirectories(uploadPath); // สร้างโฟลเดอร์หากยังไม่มี
//
//            // 📥 บันทึกไฟล์ลง disk
//            Path filePath = uploadPath.resolve(savedFilename);
//            imageFile.transferTo(filePath.toFile());
//
//            // 💾 บันทึกชื่อไฟล์ (ไม่รวม path) ลง DB
//            product.setImage(savedFilename);
//        }
//
//        // 💡 บันทึกข้อมูลสินค้า
//        productRepository.save(product);
//    }

    public List<Product> searchProducts(String searchQuery) {
        return productRepository.findByNameContainingIgnoreCase(searchQuery);  // ค้นหาสินค้าที่มีชื่อคล้ายกับคำค้น
    }

    public boolean increaseStock(Long productId, int amount) {
        Optional<Product> productOpt = productRepository.findByIdForUpdate(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStockQuantity(product.getStockQuantity() + amount);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    // ลด stock อย่างปลอดภัย
    public boolean decreaseStock(Long productId, int amount) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getStockQuantity() >= amount) {
                product.setStockQuantity(product.getStockQuantity() - amount);
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }

    // ตรวจสอบว่าสินค้าคงเหลือพอหรือไม่
    public boolean isInStock(Long productId, int quantity) {
        return productRepository.findById(productId)
                .map(product -> {
                    System.out.println("Checking stock for product " + product.getName() +
                            " | Available: " + product.getStockQuantity() +
                            " | Requested: " + quantity);
                    return product.getStockQuantity() >= quantity;
                })
                .orElse(false);
    }

    public List<Product> getTop5LowStockProducts() {
        return productRepository.findTop5ByOrderByStockQuantityAsc();
    }

    public List<Product> searchProductsByNameOrCategory(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(keyword, keyword);
    }

    @PostConstruct
    public void initUploadDir() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            uploadDir = "C:/temjaishop/uploads";
        } else {
            uploadDir = "/opt/temjaishop/uploads";
        }

        System.out.println("📂 Upload Directory: " + uploadDir);
    }

    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findByStockQuantityLessThan(threshold);
    }

    public void importStockFromExcel(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String name = row.getCell(0).getStringCellValue();
                String categoryName = row.getCell(1).getStringCellValue();
                BigDecimal price = new BigDecimal(row.getCell(2).getNumericCellValue());
                int stockQuantity = (int) row.getCell(3).getNumericCellValue();

                Category category = categoryRepository.findByName(categoryName)
                        .orElseGet(() -> categoryRepository.save(new Category(categoryName)));

                Product product = productRepository.findByName(name)
                        .orElse(new Product());
                product.setName(name);
                product.setCategory(category);
                product.setPrice(price);
                product.setStockQuantity(stockQuantity);
                productRepository.save(product);
            }
        }
    }



}

package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.repository.ProductRepository;
import com.wewe.weweShop.service.CartService;
import com.wewe.weweShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;


@Controller

public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private CartService cartService;

    @GetMapping("/products/list")
    public String productList(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product-list"; // ส่งไปที่ product-list.html
    }

    // ดึงสินค้าตามหมวดหมู่
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/AllProduct")
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/product/{id:\\d+}")
    public String getProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-detail";
    }

    @GetMapping("/products")
    public String showProductList(@RequestParam(value = "search", required = false) String searchQuery, Model model, Principal principal) {

        List<Product> products;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            // ค้นหาสินค้าตามชื่อ
            products = productService.searchProducts(searchQuery);
        } else {
            // แสดงสินค้าทั้งหมด
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("searchQuery", searchQuery);  // เก็บค่าคำค้นไว้ในโมเดลเพื่อแสดงใน input field
        return "product-list";
    }

    // ✅ แสดงสินค้าทั้งหมด
    @GetMapping("/Allproducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ✅ ดูรายละเอียดสินค้า
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ✅ เพิ่มสินค้า (เฉพาะ ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    // ✅ แก้ไขสินค้า (เฉพาะ ADMIN)  ✅ ใช้ hasAuthority ถ้า JWT เก็บเป็น "ROLE_ADMIN"
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") Product product,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, product, imageFile));
    }

    // ✅ ลบสินค้า (เฉพาะ ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    // การเพิ่มสินค้าไปที่ตะกร้า
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            @RequestParam("userEmail") String userEmail) {
        cartService.addToCart(userEmail, productId, quantity); // เพิ่มสินค้าไปที่ตะกร้า
        return "redirect:/cart"; // ไปที่หน้า Cart
    }

    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> handleFavicon() {
        return ResponseEntity.notFound().build();
    }
}


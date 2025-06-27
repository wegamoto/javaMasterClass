package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.repository.ProductRepository;
import com.wewe.temjaiShop.service.CartService;
import com.wewe.temjaiShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    @Autowired
    private ProductService productService; // ใช้สำหรับดึงข้อมูลสินค้า
    @Autowired
    private CartService cartService; // ใช้สำหรับเพิ่มสินค้าไปที่ตะกร้า

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    public ProductViewController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

//    // การแสดงรายการสินค้าทั้งหมด
//    @GetMapping
//    public String viewProducts(Model model) {
//        List<Product> products = productService.getAllProducts(); // ดึงสินค้าทั้งหมด
//        model.addAttribute("products", products); // ส่งสินค้าทั้งหมดไปยัง View
//        return "product-list"; // ชื่อไฟล์ที่ใช้แสดงผล (product-list)
//    }

//    // การเพิ่มสินค้าไปที่ตะกร้า
//    @PostMapping("/add")
//    public String addToCart(@RequestParam("productId") Long productId,
//                            @RequestParam("quantity") int quantity,
//                            @RequestParam("userEmail") String userEmail) {
//        cartService.addToCart(userEmail, productId, quantity); // เพิ่มสินค้าไปที่ตะกร้า
//        return "redirect:/cart"; // ไปที่หน้า Cart
//    }
//
//    @GetMapping("/products")
//    public String showProducts(Model model) {
//        List<Product> products = productRepository.findAll();
//        model.addAttribute("products", products);
//        return "products";
//    }
}


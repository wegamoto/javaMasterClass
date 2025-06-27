package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    @Autowired
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String viewProducts(Model model) {
        List<Product> products = productService.getAllProducts(); // is not null
        model.addAttribute("products", products);
        return "admin/product-list"; // in side templates/admin/product-list.html
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    @PostMapping("/save")
    public String saveProduct(
            @ModelAttribute("product") Product product,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        productService.saveProductWithImage(product, imageFile); // not null and connect database
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);

        System.out.println("Image filename = " + product.getImage());

        model.addAttribute("product", product);
        return "admin/product-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute("product") Product product,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        productService.updateProduct(id, product, imageFile);
        return "redirect:/admin/products";
    }

}


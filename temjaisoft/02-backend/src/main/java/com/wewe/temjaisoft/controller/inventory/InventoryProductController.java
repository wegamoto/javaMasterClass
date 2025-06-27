package com.wewe.temjaisoft.controller.inventory;

import com.wewe.temjaisoft.model.inventory.Product;
import com.wewe.temjaisoft.service.inventory.CategoryService;
import com.wewe.temjaisoft.service.inventory.ProductService;
import com.wewe.temjaisoft.service.inventory.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory/products")
public class InventoryProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    public InventoryProductController(ProductService productService,
                                      CategoryService categoryService,
                                      SupplierService supplierService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "inventory/product-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("formTitle", "เพิ่มสินค้าใหม่");
        model.addAttribute("formAction", "/inventory/products/save");
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("suppliers", supplierService.getAll());
        return "inventory/product-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบสินค้าด้วย ID: " + id));

        model.addAttribute("product", product);
        model.addAttribute("formTitle", "แก้ไขสินค้า");
        model.addAttribute("formAction", "/inventory/products/save");
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("suppliers", supplierService.getAll());
        return "inventory/product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/inventory/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/inventory/products";
    }
}


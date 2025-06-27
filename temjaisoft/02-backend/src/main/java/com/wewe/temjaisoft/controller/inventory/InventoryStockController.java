package com.wewe.temjaisoft.controller.inventory;

import com.wewe.temjaisoft.model.inventory.Product;
import com.wewe.temjaisoft.model.inventory.Stock;
import com.wewe.temjaisoft.service.inventory.ProductService;
import com.wewe.temjaisoft.service.inventory.StockService;
import com.wewe.temjaisoft.service.inventory.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/inventory/stocks")
public class InventoryStockController {

    private final StockService stockService;
    private final ProductService productService;

    public InventoryStockController(StockService stockService, ProductService productService) {
        this.stockService = stockService;
        this.productService = productService;
    }

    // 🔍 แสดงรายการสต๊อก
    @GetMapping
    public String listStocks(Model model) {
        List<Stock> stocks = stockService.findAll();
        model.addAttribute("stocks", stocks);
        return "inventory/stock-list";
    }

    // ➕ แสดงฟอร์มเพิ่มใหม่
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("stock", new Stock());
        model.addAttribute("products", productService.getAllProducts());
        return "inventory/stock-form";
    }

    // ✏️ แก้ไขข้อมูล
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id).orElseThrow(() -> new IllegalArgumentException("ไม่พบ stock id: " + id));
        model.addAttribute("stock", stock);
        model.addAttribute("products", productService.getAllProducts());
        return "inventory/stock-form";
    }

    // 💾 บันทึกข้อมูล
    @PostMapping("/save")
    public String saveStock(@ModelAttribute Stock stock) {
        stockService.save(stock);
        return "redirect:/inventory/stocks";
    }

    // ❌ ลบ
    @GetMapping("/delete/{id}")
    public String deleteStock(@PathVariable Long id) {
        stockService.deleteById(id);
        return "redirect:/inventory/stocks";
    }
}


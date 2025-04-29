package com.wewe.weweShop.controller;

import com.wewe.weweShop.dto.CartItemRequest;
import com.wewe.weweShop.dto.CartItemResponse;
import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RecommendationApiController {

    private final ProductService productService;

    public RecommendationApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/recommendations")
    public List<Product> getRecommendations(@RequestParam(defaultValue = "0") int page) {
        return productService.getRecommendations(page);
    }
//
//    @PostMapping("/cart/add")
//    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest request, Principal principal) {
//        cartService.addToCart(principal.getName(), request.getProductId(), request.getQuantity());
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/addToCart")
    @ResponseBody
    public CartItemResponse addToCart(@RequestBody CartItemRequest request, Principal principal) {
        // สมมุติว่า save cart item สำเร็จ
        // ใส่ logic บันทึกจริงได้ตามระบบคุณ

        return new CartItemResponse(true, "Product added to cart successfully!");
    }
}


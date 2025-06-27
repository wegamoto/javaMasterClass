package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.repository.UserRepository;
import com.wewe.temjaiShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class RecommendationController {

    private final ProductService productService;

    private final UserRepository userRepository;

    public RecommendationController(ProductService productService, UserRepository userRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
    }


    // เส้นทาง /recommendations
    @GetMapping("/recommendations")
    public String showRecommendations(Model model, Principal principal) {
        // ตรวจสอบว่า Principal เป็นผู้ใช้แบบ OAuth2 หรือ Local
        String username = "ผู้เยี่ยมชม";

        if (principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;

            Object principalObj = authentication.getPrincipal();

            if (principalObj instanceof UserDetails userDetails) {
                // 🔐 Local User
                username = userDetails.getUsername();
            } else if (principalObj instanceof OAuth2User oauth2User) {
                // 🌐 Facebook/Google OAuth2 User
                username = oauth2User.getAttribute("name"); // หรือใช้ "email" ถ้ามี
            }
        }

        // ตัวอย่างการใช้ username ในการดึงสินค้าเฉพาะบุคคล (สมมุติ)
        List<Product> recommendedProducts = productService.getRecommendationsForUser(username);

        model.addAttribute("recommendedProducts", recommendedProducts);
        model.addAttribute("username", username);

        return "recommendations"; // ชื่อหน้า HTML ที่จะแสดง
    }
}


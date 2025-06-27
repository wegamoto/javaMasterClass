package com.wewe.temjaiShop.service;

import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.repository.ProductRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public RecommendationService(UserRepository userRepository,
                                 ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Product> getRecommendationsForUser() {
        String username = getCurrentUsername();

        if (username == null || username.isBlank()) {
            return productRepository.findTop10ByOrderBySoldCountDesc();
        }

        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(username); // ✅ ใช้ userRepository ที่ถูก inject แล้ว
        if (optionalUser.isEmpty()) {
            return productRepository.findTop10ByOrderBySoldCountDesc();
        }

        User user = optionalUser.get();
        String favoriteCategory = user.getFavoriteCategory();

        if (favoriteCategory != null) {
            return productRepository.findTop10ByCategory_NameOrderBySoldCountDesc(favoriteCategory);
        }

        return productRepository.findTop10ByOrderBySoldCountDesc();
    }
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();  // ใช้ได้กับ email ด้วย
        } else {
            return principal.toString();
        }
    }
}


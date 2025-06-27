package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.repository.ProductRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import com.wewe.temjaiShop.service.CartService;
import com.wewe.temjaiShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final UserRepository userRepository;

    @Autowired
    public ProductController(ProductRepository productRepository,
                             ProductService productService,
                             CartService cartService,
                             UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @GetMapping("/products/list")
    public String productList(Model model, Authentication authentication) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        addCartItemCountToModel(model, authentication);
        return "product-list";
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/AllProduct")
    public String getAllProducts(Model model, Authentication authentication) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        addCartItemCountToModel(model, authentication);
        return "products";
    }

    @GetMapping("/product/{id:\\d+}")
    public String getProductDetail(@PathVariable Long id, Model model, Authentication authentication) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        addCartItemCountToModel(model, authentication);
        return "product-detail";
    }

    @GetMapping("/products")
    public String showProductList(@RequestParam(value = "search", required = false) String searchQuery,
                                  Model model,
                                  Authentication authentication) {
        List<Product> products;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            products = productService.searchProducts(searchQuery);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("searchQuery", searchQuery);
        addCartItemCountToModel(model, authentication);
        return "product-list";
    }

    @GetMapping("/Allproducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") Product product,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, product, imageFile));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    // ✅ เพิ่มสินค้าไปยังตะกร้า พร้อมตรวจสอบ และแสดงข้อความผลลัพธ์
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            Principal principal,
                            Model model) {
        String userEmail = extractEmailFromPrincipal(principal);
        if (userEmail == null) {
            return "redirect:/cart/view?message=ไม่สามารถดึงอีเมลผู้ใช้ได้";
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            return "redirect:/cart/view?message=ไม่พบสินค้าที่ต้องการเพิ่ม";
        }

        try {
            cartService.addProductToCart(productId, quantity, principal);
            return "redirect:/cart/view?message=เพิ่มสินค้า \"" + product.getName() + "\" จำนวน " + quantity + " ชิ้น ไปยังตะกร้าแล้ว";
        } catch (Exception e) {
            // แสดงข้อความ error แบบ user friendly
            return "redirect:/cart/view?message=เกิดข้อผิดพลาดในการเพิ่มสินค้า กรุณาลองใหม่อีกครั้ง";
        }
    }


    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> handleFavicon() {
        return ResponseEntity.notFound().build();
    }

    // ✅ เมธอดช่วยดึงจำนวนสินค้าในตะกร้า โดยใช้ Principal
    private void addCartItemCountToModel(Model model, Principal principal) {
        try {
            if (principal != null) {
                String email = principal.getName();
                if (email != null) {
                    User user = userRepository.findByUsernameOrEmail(email).orElse(null);
                    if (user != null) {
                        int itemCount = cartService.countItemsInCart(user.getId());
                        model.addAttribute("cartItemCount", itemCount);
                        return;
                    }
                }
            }
            model.addAttribute("cartItemCount", 0);
        } catch (Exception e) {
            model.addAttribute("cartItemCount", 0);
        }
    }

    // ✅ เมธอดช่วยดึงอีเมลผู้ใช้
    private String extractUserEmail(Authentication authentication) {
        if (authentication == null) return null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            return (String) ((OAuth2User) principal).getAttributes().get("email");
        }
        return null;
    }

    private String extractEmailFromPrincipal(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauth2Token) {
            OAuth2User oauth2User = oauth2Token.getPrincipal();
            Object emailAttr = oauth2User.getAttributes().get("email");
            if (emailAttr != null) {
                return emailAttr.toString();
            } else {
                throw new IllegalStateException("ไม่สามารถดึงอีเมลจากบัญชี OAuth2 ได้");
            }
        }

        // fallback: ใช้ชื่อผู้ใช้แบบ form login (username เป็น email)
        return principal.getName();
    }

}

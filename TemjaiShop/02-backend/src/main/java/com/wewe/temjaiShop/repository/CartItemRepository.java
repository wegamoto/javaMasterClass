package com.wewe.temjaiShop.repository;

import com.wewe.temjaiShop.model.CartItem;
import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.model.User;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // =========================
    // === FIND Methods ========
    // =========================

    // แนะนำ (อิง userId แทน)
    List<CartItem> findByUser_Id(Long userId);

    // หารายการในตะกร้าทั้งหมดของผู้ใช้จาก email
    List<CartItem> findByUser_Email(String userEmail);

    // หารายการในตะกร้างทั้งหมดของผู้ใช้จาก username
    List<CartItem> findByUser_Username(String username);

    Optional<CartItem> findByIdAndUserId(Long cartItemId, Long userId);

    @Query("select ci from CartItem ci left join ci.user u where u.username = :username")
    List<CartItem> findByUserUsername(@Param("username") String username);

    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.user.id = :userId")
    List<CartItem> getCartItemsByUserId(@Param("userId") Long userId);

    // หารายการในตะกร้าทั้งหมดของผู้ใช้จาก entity User
    List<CartItem> findByUser(User user);

    // หารายการในตะกร้าที่มี product เฉพาะตัว (อาจไม่เจอถ้า product ไม่มีในตะกร้า)
    Optional<CartItem> findByProduct(Product product);

    // หา CartItem จาก user + product (กันสินค้าซ้ำในตะกร้า)
    Optional<CartItem> findByUser_EmailAndProduct_Id(String email, Long productId);

    // หา CartItem จาก user + product (กันสินค้าซ้ำในตะกร้า 2)
    Optional<CartItem> findByUserAndProduct(User user, Product product);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")}) // 3 วินาที
    Optional<CartItem> findByUser_IdAndProduct_Id(Long userId, Long productId);

    // 🔒 ใช้ pessimistic lock สำหรับกัน race condition
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CartItem c WHERE c.user.id = :userId AND c.product.id = :productId")
    Optional<CartItem> findWithLockByUserIdAndProductId(Long userId, Long productId);

    Optional<CartItem> findByIdAndUser(Long id, User user);

    // กรณีอยากเช็คก่อน insert หรือ update:
    boolean existsByUserAndProduct(User user, Product product);

    // =========================
    // === COUNT Methods =======
    // =========================

    // นับจำนวน CartItem ของ User
    int countByUser_Id(Long userId);

    int countByUser_Username(String username);

//    Integer countByUser_Id(Long userId);

    // =========================
    // === SUM Methods =========
    // =========================

    @Query("SELECT SUM(c.quantity) FROM CartItem c WHERE c.user.id = :userId")
    Integer sumQuantityByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(c.quantity) FROM CartItem c WHERE c.user.email = :userEmail")
    Integer sumQuantityByUserEmail(@Param("userEmail") String userEmail);

    // =========================
    // === UPDATE Methods ======
    // =========================

    // ปรับจำนวนสินค้าในตะกร้าโดยระบุ user + product
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.user.email = :userEmail AND c.product.id = :productId")
    void updateCartItemQuantity(@Param("userEmail") String userEmail, @Param("productId") Long productId, @Param("quantity") int quantity);

    @Modifying
    @Transactional
    @Query("""
    UPDATE CartItem c SET c.quantity = c.quantity + :quantityToAdd
    WHERE c.user.id = :userId AND c.product.id = :productId
""")
    int updateQuantityIfExists(@Param("userId") Long userId,
                               @Param("productId") Long productId,
                               @Param("quantityToAdd") int quantityToAdd);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO cart_items (price, product_id, product_name, quantity, user_id)
        VALUES (:price, :productId, :productName, :quantity, :userId)
        ON DUPLICATE KEY UPDATE quantity = quantity + :quantity
        """, nativeQuery = true)
    void upsertCartItem(@Param("price") BigDecimal price,
                        @Param("productId") Long productId,
                        @Param("productName") String productName,
                        @Param("quantity") int quantity,
                        @Param("userId") Long userId);

    @Modifying
    @Query(value = "INSERT INTO cart_items (price, product_id, product_name, quantity, user_id) " +
            "VALUES (:price, :productId, :productName, :quantity, :userId) " +
            "ON DUPLICATE KEY UPDATE quantity = quantity + :quantity", nativeQuery = true)
    void insertOrUpdateCartItem(@Param("price") BigDecimal price,
                                @Param("productId") Long productId,
                                @Param("productName") String productName,
                                @Param("quantity") int quantity,
                                @Param("userId") Long userId);

    // =========================
    // === DELETE Methods ======
    // =========================

    // ลบรายการตะกร้าทั้งหมดของ user
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM CartItem c WHERE c.user.email = :userEmail")
    void deleteByUser_Email(@Param("userEmail") String userEmail);

    // ลบ CartItem จาก product ที่ถูกลบ (ทุก user)
    void deleteByProduct_Id(Long productId);

    // ลบ CartItem ทั้งหมดของ User (Spring Data JPA มีเมธอด deleteAllBy... ให้ใช้)
    void deleteAllByUser(User user);

    // รวมจำนวนทั้งหมดของสินค้าทั้งหมดในตะกร้า
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.user = :user")
    Integer sumQuantityByUser(@Param("user") User user);


    void deleteByUserId(Long userId);
}

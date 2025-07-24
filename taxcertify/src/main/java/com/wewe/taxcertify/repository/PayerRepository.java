package com.wewe.taxcertify.repository;

import com.wewe.taxcertify.model.Payer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayerRepository extends JpaRepository<Payer, Long> {

    // ตัวอย่าง method เพิ่มเติมที่อาจเป็นประโยชน์ในอนาคต
    Optional<Payer> findByName(String name);

    boolean existsByTaxId(String taxId);
}

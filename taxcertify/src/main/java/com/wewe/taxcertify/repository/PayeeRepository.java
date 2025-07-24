package com.wewe.taxcertify.repository;

import com.wewe.taxcertify.model.Payee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayeeRepository extends JpaRepository<Payee, Long> {

    // สามารถเพิ่ม method เพิ่มเติมได้ เช่น
    Payee findByTaxId(String taxId);
}

package com.wewe.proflow.repository;

import com.wewe.proflow.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    // JpaRepository มี method พื้นฐาน เช่น findAll(), findById(), save() อยู่แล้ว
}

package com.wewe.springlance.repository;

import com.wewe.springlance.model.Message;
import com.wewe.springlance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByProjectIdOrderBySentAtAsc(Long projectId);

    // ✅ นับจำนวนข้อความที่ยังไม่ได้อ่านโดยผู้ใช้คนนี้
    int countByReceiverAndIsReadFalse(User receiver);
}

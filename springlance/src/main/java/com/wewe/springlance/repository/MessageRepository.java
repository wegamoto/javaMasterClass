package com.wewe.springlance.repository;

import com.wewe.springlance.model.Message;
import com.wewe.springlance.model.ProjectRequest;
import com.wewe.springlance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByProjectIdOrderBySentAtAsc(Long projectId);

    // ✅ นับจำนวนข้อความที่ยังไม่ได้อ่านโดยผู้ใช้คนนี้
    int countByReceiverAndIsReadFalse(User receiver);

    List<Message> findBySenderOrReceiver(User sender, User receiver);

    @Query("""
    select p from ProjectRequest p
    where p in (
        select m.project from Message m where m.sender = :user or m.receiver = :user
    )
    order by (
        select max(m2.sentAt) from Message m2 where m2.project = p
    ) desc
""")
    List<ProjectRequest> findProjectsByUserOrderByLatestMessage(@Param("user") User user);

}

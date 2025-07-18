package com.wewe.springlance.service;

import com.wewe.springlance.model.Message;
import com.wewe.springlance.model.ProjectRequest;
import com.wewe.springlance.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageService {
    Message save(Message message);
    List<Message> findByProjectId(Long projectId);
    // เพิ่มเมธอดนี้
    List<ProjectRequest> findProjectsByUser(User user);

    @Query("SELECT DISTINCT m.project FROM Message m WHERE m.sender = :user OR m.receiver = :user ORDER BY m.sentAt DESC")
    List<ProjectRequest> findDistinctProjectsBySenderOrReceiver(@Param("user") User user1, @Param("user") User user2);
}

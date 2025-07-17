package com.wewe.springlance.service;

import com.wewe.springlance.model.Message;

import java.util.List;

public interface MessageService {
    Message save(Message message);
    List<Message> findByProjectId(Long projectId);
}

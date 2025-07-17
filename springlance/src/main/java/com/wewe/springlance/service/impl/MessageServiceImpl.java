package com.wewe.springlance.service.impl;

import com.wewe.springlance.model.Message;
import com.wewe.springlance.repository.MessageRepository;
import com.wewe.springlance.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository repository;

    public MessageServiceImpl(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Message save(Message message) {
        return repository.save(message);
    }

    @Override
    public List<Message> findByProjectId(Long projectId) {
        return repository.findByProjectIdOrderBySentAtAsc(projectId);
    }
}

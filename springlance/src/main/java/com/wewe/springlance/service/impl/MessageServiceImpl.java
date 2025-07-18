package com.wewe.springlance.service.impl;

import com.wewe.springlance.model.Message;
import com.wewe.springlance.model.ProjectRequest;
import com.wewe.springlance.model.User;
import com.wewe.springlance.repository.MessageRepository;
import com.wewe.springlance.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository repository, MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findByProjectId(Long projectId) {
        return messageRepository.findByProjectIdOrderBySentAtAsc(projectId);
    }

    @Override
    public List<ProjectRequest> findProjectsByUser(User user) {
        return messageRepository.findProjectsByUserOrderByLatestMessage(user);
    }

    @Override
    public List<ProjectRequest> findDistinctProjectsBySenderOrReceiver(User user1, User user2) {
        return List.of();
    }

}

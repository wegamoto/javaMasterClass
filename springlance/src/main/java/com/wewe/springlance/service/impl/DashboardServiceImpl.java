package com.wewe.springlance.service.impl;

import com.wewe.springlance.model.User;
import com.wewe.springlance.repository.*;
import com.wewe.springlance.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ProjectRequestRepository projectRequestRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public int countProjectsByUser(Optional<User> user) {
        return projectRequestRepository.countByClient(user);
    }

    @Override
    public int countUnreadMessagesByUser(Optional<User> user) {
        return messageRepository.countByReceiverAndIsReadFalse(user);
    }

    @Override
    public int countPendingInvoicesByUser(User user) {
        return invoiceRepository.countByProjectClientAndIsPaidFalse(user);
    }

    @Override
    public double averageRatingForUser(User user) {
        Double avg = reviewRepository.averageRatingByFreelancer(user);
        return avg != null ? avg : 0.0;
    }
}


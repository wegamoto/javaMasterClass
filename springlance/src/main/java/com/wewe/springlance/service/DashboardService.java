package com.wewe.springlance.service;

import com.wewe.springlance.model.User;

import java.util.Optional;

public interface DashboardService {
    int countProjectsByUser(Optional<User> user);
    int countUnreadMessagesByUser(Optional<User> user);
    int countPendingInvoicesByUser(User user);
    double averageRatingForUser(User user);
}

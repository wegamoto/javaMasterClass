package com.wewe.springlance.service;

import com.wewe.springlance.model.User;

public interface DashboardService {
    int countProjectsByUser(User user);
    int countUnreadMessagesByUser(User user);
    int countPendingInvoicesByUser(User user);
    double averageRatingForUser(User user);
}

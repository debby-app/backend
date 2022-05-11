package com.project.debby.domain.user.service;

import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.user.model.Notification;
import com.project.debby.domain.user.model.NotificationType;
import com.project.debby.domain.user.model.User;

import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotifications(String userID);
    Notification notify(User user, NotificationType type, LoanState state);
}

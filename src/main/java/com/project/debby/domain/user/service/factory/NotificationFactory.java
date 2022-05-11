package com.project.debby.domain.user.service.factory;

import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.user.model.Notification;
import com.project.debby.domain.user.model.NotificationType;
import com.project.debby.domain.user.model.User;

public interface NotificationFactory {
    Notification create(NotificationType type, LoanState state, User user);
}

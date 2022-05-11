package com.project.debby.domain.user.service.factory;

import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.user.model.Notification;
import com.project.debby.domain.user.model.NotificationType;
import com.project.debby.domain.user.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationFactoryImpl implements NotificationFactory {
    @Override
    public Notification create(NotificationType type, LoanState state, User user) {
        return Notification.builder()
                .correlatedLoanState(state)
                .timestamp(LocalDateTime.now())
                .user(user)
                .type(type)
                .build();
    }
}

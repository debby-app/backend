package com.project.debby.domain.user.service;

import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.user.model.Notification;
import com.project.debby.domain.user.model.NotificationType;
import com.project.debby.domain.user.model.User;
import com.project.debby.domain.user.model.repository.NotificationRepository;
import com.project.debby.domain.user.service.factory.NotificationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationFactory notificationFactory;

    @Override
    public List<Notification> getAllNotifications(String userID) {
        return notificationRepository.getAllByUser_UserDetails_Credentials_ExternalId(userID);
    }

    @Override
    public Notification notify(User user, NotificationType type, LoanState state) {
        Notification notification = notificationFactory.create(type, state, user);
        return notificationRepository.saveAndFlush(notification);
    }
}

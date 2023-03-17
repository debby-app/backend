package com.project.debby.domain.user.model.repository;

import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.user.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getAllByUser_UserDetails_Credentials_ExternalId(String externalID);
}

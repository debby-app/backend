package com.project.debby.domain.user.dto.response;

import com.project.debby.domain.user.model.Notification;
import com.project.debby.domain.user.model.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class NotificationDTO {
    private NotificationType type;
    private Long loanStateID;
    private LocalDateTime timestamp;

    public static NotificationDTO create(Notification notification){
        return NotificationDTO.builder()
                .type(notification.getType())
                .timestamp(notification.getTimestamp())
                .loanStateID(notification.getCorrelatedLoanState().getId())
                .build();
    }
}

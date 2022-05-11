package com.project.debby.domain.user.dto.request;

import lombok.Data;

@Data
public class SettingsUpdateDTO {
    private boolean isEmailNotificationEnabled;
    private boolean isPushNotificationEnabled;
}

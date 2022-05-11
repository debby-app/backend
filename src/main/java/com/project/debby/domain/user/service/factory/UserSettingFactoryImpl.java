package com.project.debby.domain.user.service.factory;

import com.project.debby.domain.user.model.UserSettings;
import org.springframework.stereotype.Service;

@Service
public class UserSettingFactoryImpl implements UserSettingFactory {
    @Override
    public UserSettings createDefault() {
        return UserSettings.builder()
                .isEmailNotificationEnabled(true)
                .isPushNotificationEnabled(true)
                .build();
    }
}

package com.project.debby.domain.user.service.factory;

import com.project.debby.domain.auth.service.factory.UserDetailsFactory;
import com.project.debby.domain.user.dto.request.UserRegisterDTO;
import com.project.debby.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserFactoryImpl implements UserFactory {

    private final UserDetailsFactory userDetailsFactory;
    private final UserSettingFactory userSettingFactory;

    @Override
    public User create(UserRegisterDTO registerDTO) {
        return User.builder()
                .username(registerDTO.getEmail().split("@")[0])
                .userDetails(userDetailsFactory.create(registerDTO))
                .settings(userSettingFactory.createDefault())
                .build();
    }
}

package com.project.debby.domain.auth.service.factory;

import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.domain.auth.model.UserRole;
import com.project.debby.domain.user.dto.request.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class UserDetailsFactoryImpl implements UserDetailsFactory {

    private final CredentialsFactory credentialsFactory;

    @Override
    public UserDetails create(UserRegisterDTO registerDTO) {
        return UserDetails.builder()
                .credentials(credentialsFactory.create(registerDTO))
                .isEnabled(false)
                .isExpired(false)
                .isLocked(false)
                .role(UserRole.user)
                .token(new ArrayList<>())
                .build();
    }
}

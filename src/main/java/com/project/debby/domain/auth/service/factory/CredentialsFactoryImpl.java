package com.project.debby.domain.auth.service.factory;

import com.project.debby.domain.auth.model.Credentials;
import com.project.debby.domain.user.dto.request.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CredentialsFactoryImpl implements CredentialsFactory {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Credentials create(UserRegisterDTO registerDTO) {
        return Credentials.builder()
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .externalId(UUID.randomUUID().toString())
                .isExpired(false)
                .build();
    }
}

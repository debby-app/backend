package com.project.debby.domain.auth.service.factory;

import com.project.debby.domain.auth.model.Token;
import org.springframework.stereotype.Service;

@Service
public class TokenFactoryImpl implements TokenFactory {
    @Override
    public Token create(String accessToken, String refreshToken) {
        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

package com.project.debby.domain.auth.service.factory;

import com.project.debby.domain.auth.model.Token;

public interface TokenFactory {
    Token create(String accessToken, String refreshToken);
}

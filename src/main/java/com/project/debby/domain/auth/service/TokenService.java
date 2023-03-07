package com.project.debby.domain.auth.service;

import com.project.debby.domain.auth.model.Token;
import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.util.exceptions.RequestedEntityNotFound;

public interface TokenService {
    Token createToken(UserDetails userDetails);
    void deleteToken(String refreshToken) throws RequestedEntityNotFound;

    Token refreshToken(String refreshToken) throws RequestedEntityNotFound;
}

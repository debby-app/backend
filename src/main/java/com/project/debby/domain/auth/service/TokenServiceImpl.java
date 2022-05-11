package com.project.debby.domain.auth.service;

import com.project.debby.domain.auth.model.Token;
import com.project.debby.domain.auth.model.TokenType;
import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.domain.auth.model.repository.TokenRepository;
import com.project.debby.domain.auth.model.repository.UserDetailsRepository;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import com.project.debby.util.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final UserDetailsRepository userDetailsRepository;
    private final TokenRepository tokenRepository;


    @Override
    public Token createToken(UserDetails userDetails) {
        Token token = new Token();
        token.setAccessToken(jwtService.generateToken(userDetails, TokenType.access));
        token.setRefreshToken(jwtService.generateToken(userDetails, TokenType.refresh));
        userDetails.getToken().add(token);
        userDetailsRepository.saveAndFlush(userDetails);
        return token;
    }

    @Transactional
    @Override
    public void deleteToken(String refreshToken) throws RequestedEntityNotFound {
        Token token = tokenRepository.getTokenByRefreshToken(refreshToken)
                .orElseThrow(RequestedEntityNotFound::new);
        tokenRepository.deleteFromAssociation(token.getId());
        tokenRepository.delete(token);
    }
}

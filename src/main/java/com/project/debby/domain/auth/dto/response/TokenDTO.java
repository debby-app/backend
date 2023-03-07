package com.project.debby.domain.auth.dto.response;

import com.project.debby.domain.auth.model.Token;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenDTO {
    private String accessToken;
    private String refreshToken;

    public static TokenDTO create(Token token){
        return TokenDTO.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}

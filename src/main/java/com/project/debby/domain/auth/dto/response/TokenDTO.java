package com.project.debby.domain.auth.dto.response;

import com.project.debby.domain.auth.model.Token;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
    private String userID;

    public static TokenDTO create(Token token, String userID){
        return TokenDTO.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .userID(userID)
                .build();
    }
}

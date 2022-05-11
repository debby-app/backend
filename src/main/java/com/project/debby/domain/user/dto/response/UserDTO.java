package com.project.debby.domain.user.dto.response;

import com.project.debby.domain.user.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String username;
    private String externalId;

    public static UserDTO create(User user){
        return UserDTO.builder()
                .username(user.getUsername())
                .externalId(user.getUserDetails().getCredentials().getExternalId())
                .build();
    }
}

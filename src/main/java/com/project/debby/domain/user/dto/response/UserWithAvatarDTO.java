package com.project.debby.domain.user.dto.response;

import com.project.debby.domain.user.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserWithAvatarDTO {
    private String username;
    private String externalId;
    private String avatarLink;

    public static UserWithAvatarDTO create(User user, String avatarLink){
        return UserWithAvatarDTO.builder()
                .username(user.getUsername())
                .externalId(user.getUserDetails().getCredentials().getExternalId())
                .avatarLink(avatarLink)
                .build();
    }
}

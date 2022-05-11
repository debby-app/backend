package com.project.debby.domain.user.dto.response;

import com.project.debby.domain.user.model.Group;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class GroupDTO {
    private Long groupId;
    private String name;
    private List<UserDTO> users;

    public static GroupDTO create(Group group){
        return GroupDTO.builder()
                .groupId(group.getId())
                .name(group.getName())
                .users(group.getUsers().stream().map(UserDTO::create).collect(Collectors.toList()))
                .build();
    }
}

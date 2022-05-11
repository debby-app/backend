package com.project.debby.domain.user.dto.request;

import lombok.Data;

@Data
public class AddMemberToGroupDTO {
    private String groupName;
    private String user;
}

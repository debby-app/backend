package com.project.debby.domain.user.dto.request;

import lombok.Data;

@Data
public class DeleteMemberFromGroupDTO {
    private String groupName;
    private String user;
}

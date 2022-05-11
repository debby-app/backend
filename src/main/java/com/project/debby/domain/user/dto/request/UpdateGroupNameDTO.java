package com.project.debby.domain.user.dto.request;

import lombok.Data;

@Data
public class UpdateGroupNameDTO {
    private String oldName;
    private String newName;
}

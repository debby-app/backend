package com.project.debby.domain.user.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class GroupRegisterDTO {
    private String name;
    private List<String> users;
}

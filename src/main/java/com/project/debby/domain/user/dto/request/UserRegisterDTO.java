package com.project.debby.domain.user.dto.request;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String email;
    private String password;
}

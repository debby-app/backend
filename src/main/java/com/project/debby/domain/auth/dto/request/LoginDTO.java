package com.project.debby.domain.auth.dto.request;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;
}

package com.project.debby.domain.auth.service.factory;

import com.project.debby.domain.auth.model.Credentials;
import com.project.debby.domain.user.dto.request.UserRegisterDTO;

public interface CredentialsFactory {
    Credentials create(UserRegisterDTO registerDTO);
}

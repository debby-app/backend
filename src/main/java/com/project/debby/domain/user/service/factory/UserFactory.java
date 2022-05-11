package com.project.debby.domain.user.service.factory;

import com.project.debby.domain.user.model.User;
import com.project.debby.domain.user.dto.request.UserRegisterDTO;

public interface UserFactory {
    User create(UserRegisterDTO registerDTO);
}

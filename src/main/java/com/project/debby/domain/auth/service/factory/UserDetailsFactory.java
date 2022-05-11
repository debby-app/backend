package com.project.debby.domain.auth.service.factory;

import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.domain.user.dto.request.UserRegisterDTO;

public interface UserDetailsFactory{
    UserDetails create(UserRegisterDTO registerDTO);
}

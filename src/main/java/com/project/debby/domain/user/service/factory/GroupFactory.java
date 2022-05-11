package com.project.debby.domain.user.service.factory;

import com.project.debby.domain.user.model.Group;
import com.project.debby.domain.user.model.User;
import com.project.debby.domain.user.dto.request.GroupRegisterDTO;

import java.util.List;

public interface GroupFactory {
    Group create(GroupRegisterDTO registerDTO, User owner, List<User> users);
}

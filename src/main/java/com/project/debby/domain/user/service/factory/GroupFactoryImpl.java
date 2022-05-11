package com.project.debby.domain.user.service.factory;

import com.project.debby.domain.user.dto.request.GroupRegisterDTO;
import com.project.debby.domain.user.model.Group;
import com.project.debby.domain.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupFactoryImpl implements GroupFactory {
    @Override
    public Group create(GroupRegisterDTO registerDTO, User owner, List<User> users) {
        return Group.builder()
                .name(registerDTO.getName())
                .owner(owner)
                .users(users)
                .build();
    }
}

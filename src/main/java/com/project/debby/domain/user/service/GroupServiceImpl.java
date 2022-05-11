package com.project.debby.domain.user.service;

import com.project.debby.domain.user.dto.request.AddMemberToGroupDTO;
import com.project.debby.domain.user.dto.request.DeleteMemberFromGroupDTO;
import com.project.debby.domain.user.dto.request.GroupRegisterDTO;
import com.project.debby.domain.user.dto.request.UpdateGroupNameDTO;
import com.project.debby.domain.user.model.Group;
import com.project.debby.domain.user.model.User;
import com.project.debby.domain.user.model.repository.GroupRepository;
import com.project.debby.domain.user.service.factory.GroupFactory;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupServiceImpl implements GroupService {

    private final UserService userService;
    private final GroupRepository groupRepository;
    private final GroupFactory groupFactory;

    @Override
    public Group getGroup(String name, String userID) throws RequestedEntityNotFound {
        return groupRepository.findByNameAndOwner_UserDetails_Credentials_ExternalId(name, userID)
                .orElseThrow(RequestedEntityNotFound::new);
    }

    @Override
    public List<Group> getAllGroups(String userID) {
        return groupRepository.getAllByOwner_UserDetails_Credentials_ExternalId(userID);
    }

    @Override
    public Group createGroup(GroupRegisterDTO registerDTO, String userID) throws RequestedEntityNotFound {
        User owner = userService.getUser(userID);
        List<User> members = new ArrayList<>();
        for (String id : registerDTO.getUsers()) {
            members.add(userService.getUser(id));
        }
        Group group = groupFactory.create(registerDTO, owner, members);
        return groupRepository.saveAndFlush(group);
    }

    @Override
    public void addToGroup(AddMemberToGroupDTO addMemberDTO, String userID) throws RequestedEntityNotFound {
        Group group = getGroup(addMemberDTO.getGroupName(), userID);
        group.getUsers().add(userService.getUser(addMemberDTO.getUser()));
        groupRepository.saveAndFlush(group);
    }

    @Override
    public void deleteMemberFromGroup(DeleteMemberFromGroupDTO deleteDTO, String userID) throws RequestedEntityNotFound {
        Group group = getGroup(deleteDTO.getGroupName(), userID);
        group.getUsers().removeIf((user) -> deleteDTO.getUser().equals(user.getUserDetails().getUsername()));
        groupRepository.saveAndFlush(group);
    }

    @Override
    public void updateGroupName(UpdateGroupNameDTO groupNameDTO, String userID) throws RequestedEntityNotFound {
        Group group = getGroup(groupNameDTO.getOldName(), userID);
        group.setName(groupNameDTO.getNewName());
        groupRepository.saveAndFlush(group);
    }
}

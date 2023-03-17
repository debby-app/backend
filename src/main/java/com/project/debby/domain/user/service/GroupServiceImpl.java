package com.project.debby.domain.user.service;

import com.project.debby.domain.user.dto.request.GroupRegisterDTO;
import com.project.debby.domain.user.dto.request.UpdateGroupNameDTO;
import com.project.debby.domain.user.model.Group;
import com.project.debby.domain.user.model.User;
import com.project.debby.domain.user.model.repository.GroupRepository;
import com.project.debby.domain.user.service.factory.GroupFactory;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupServiceImpl implements GroupService {

    private final UserService userService;
    private final GroupRepository groupRepository;
    private final GroupFactory groupFactory;

    @Override
    public Group getGroup(String name, String userID) throws RequestedEntityNotFound {
        return groupRepository.findByNameAndOwner_UserDetails_Credentials_ExternalId(name, userID)
                .orElseThrow(() -> new RequestedEntityNotFound("Group of " + userID + " with name" + name + " not found"));
    }

    @Override
    public List<Group> getAllGroups(String userID) {
        return groupRepository.getAllByOwner_UserDetails_Credentials_ExternalId(userID);
    }

    @Override
    public Group createGroup(GroupRegisterDTO registerDTO, String userID) throws RequestedEntityNotFound {
        log.debug("--creating group | owner extId {} name {}", userID, registerDTO.getName());
        User owner = userService.getUser(userID);
        List<User> members = new ArrayList<>();
        for (String id : registerDTO.getUsers()) {
            members.add(userService.getUser(id));
        }
        Group group = groupFactory.create(registerDTO, owner, members);
        return groupRepository.saveAndFlush(group);
    }

    @Override
    public void addToGroup(String ownerID, String groupName, String idToAdd) throws RequestedEntityNotFound {
        log.debug("--adding user to group | owner extId {} name {} member {}", ownerID, groupName, idToAdd);
        Group group = getGroup(groupName, ownerID);
        group.getUsers().add(userService.getUser(idToAdd));
        groupRepository.saveAndFlush(group);
    }

    @Override
    public void deleteMemberFromGroup(String ownerId, String groupName, String idToDelete) throws RequestedEntityNotFound {
        log.debug("--deleting user from group | owner extId {} name {} member {}", ownerId, groupName, idToDelete);
        Group group = getGroup(groupName, ownerId);
        group.getUsers().removeIf((user) -> idToDelete.equals(user.getUserDetails().getUsername()));
        groupRepository.saveAndFlush(group);
    }

    @Override
    public void updateGroupName(String ownerId, String oldName, UpdateGroupNameDTO groupNameDTO) throws RequestedEntityNotFound {
        log.debug("--updating group name | owner extId {} name {} newName {}", ownerId, oldName, groupNameDTO.getNewName());
        Group group = getGroup(oldName, ownerId);
        group.setName(groupNameDTO.getNewName());
        groupRepository.saveAndFlush(group);
    }
}

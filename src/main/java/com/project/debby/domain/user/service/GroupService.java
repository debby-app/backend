package com.project.debby.domain.user.service;

import com.project.debby.domain.user.dto.request.AddMemberToGroupDTO;
import com.project.debby.domain.user.dto.request.DeleteMemberFromGroupDTO;
import com.project.debby.domain.user.dto.request.GroupRegisterDTO;
import com.project.debby.domain.user.dto.request.UpdateGroupNameDTO;
import com.project.debby.domain.user.model.Group;
import com.project.debby.util.exceptions.RequestedEntityNotFound;

import java.util.List;

public interface GroupService {
    Group getGroup(String name, String userID) throws RequestedEntityNotFound;
    List<Group> getAllGroups(String userID);
    Group createGroup(GroupRegisterDTO registerDTO, String userID) throws RequestedEntityNotFound;
    void addToGroup(AddMemberToGroupDTO addMemberDTO, String userID) throws RequestedEntityNotFound;
    void deleteMemberFromGroup(DeleteMemberFromGroupDTO deleteDTO, String userID) throws RequestedEntityNotFound;
    void updateGroupName(UpdateGroupNameDTO groupNameDTO, String userID) throws RequestedEntityNotFound;
}

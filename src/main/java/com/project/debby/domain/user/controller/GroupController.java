package com.project.debby.domain.user.controller;

import com.project.debby.domain.user.dto.request.AddMemberToGroupDTO;
import com.project.debby.domain.user.dto.request.DeleteMemberFromGroupDTO;
import com.project.debby.domain.user.dto.request.GroupRegisterDTO;
import com.project.debby.domain.user.dto.request.UpdateGroupNameDTO;
import com.project.debby.domain.user.dto.response.GroupDTO;
import com.project.debby.domain.user.model.Group;
import com.project.debby.domain.user.service.GroupService;
import com.project.debby.util.service.request.ExternalIdExtractor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    @SneakyThrows
    @GetMapping("/group")
    public ResponseEntity<GroupDTO> getGroup(@RequestParam String name, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        return ResponseEntity.ok(GroupDTO.create(groupService.getGroup(name, externalId)));
    }

    @SneakyThrows
    @GetMapping("/groups")
    public ResponseEntity<List<GroupDTO>> getGroup(HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        return ResponseEntity.ok(groupService.getAllGroups(externalId).stream()
                .map(GroupDTO::create).collect(Collectors.toList()));
    }

    @SneakyThrows
    @PostMapping("/group")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupRegisterDTO registerDTO, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        Group group = groupService.createGroup(registerDTO, externalId);
        return ResponseEntity.ok(GroupDTO.create(group));
    }

    @SneakyThrows
    @PostMapping("/group/add")
    public ResponseEntity<Void> addMemberToGroup(@RequestBody AddMemberToGroupDTO addMemberToGroupDTO,
                                                HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        groupService.addToGroup(addMemberToGroupDTO, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/group/delete")
    public ResponseEntity<Void> deleteMemberFromGroup(@RequestBody DeleteMemberFromGroupDTO deleteMemberFromGroupDTO,
                                                HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        groupService.deleteMemberFromGroup(deleteMemberFromGroupDTO, externalId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/group/name")
    public ResponseEntity<Void> updateGroupName(@RequestBody UpdateGroupNameDTO updateGroupNameDTO,
                                                HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalID(request);
        groupService.updateGroupName(updateGroupNameDTO, externalId);
        return ResponseEntity.ok().build();
    }
}

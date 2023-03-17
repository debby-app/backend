package com.project.debby.domain.user.controller;

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
@RequestMapping("{id}/groups")
public class GroupController {

    private final GroupService groupService;

    @SneakyThrows
    @GetMapping("/{name}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable String id, @PathVariable String name, HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalIDSafely(id, request);
        return ResponseEntity.ok(GroupDTO.create(groupService.getGroup(name, externalId)));
    }

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<List<GroupDTO>> getGroups(@PathVariable String id,  HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalIDSafely(id, request);
        return ResponseEntity.ok(groupService.getAllGroups(externalId).stream()
                .map(GroupDTO::create).collect(Collectors.toList()));
    }

    @SneakyThrows
    @PostMapping("/")
    public ResponseEntity<GroupDTO> createGroup(@PathVariable String id, @RequestBody GroupRegisterDTO registerDTO,
                                                HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalIDSafely(id, request);
        Group group = groupService.createGroup(registerDTO, externalId);
        return ResponseEntity.ok(GroupDTO.create(group));
    }

    @SneakyThrows
    @PostMapping("/{name}/{idToAdd}")
    public ResponseEntity<Void> addMemberToGroup(@PathVariable String id, @PathVariable String name,
                                                 @PathVariable String idToAdd,
                                                HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalIDSafely(id, request);
        groupService.addToGroup(externalId, name, idToAdd);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @DeleteMapping("/{name}/{idToDelete}")
    public ResponseEntity<Void> deleteMemberFromGroup(@PathVariable String id, @PathVariable String name,
                                                      @PathVariable String idToDelete,
                                                HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalIDSafely(id, request);
        groupService.deleteMemberFromGroup(externalId, name, idToDelete);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PutMapping("/{name}")
    public ResponseEntity<Void> updateGroupName(@PathVariable String id, @PathVariable String name,
                                                @RequestBody UpdateGroupNameDTO updateGroupNameDTO,
                                                HttpServletRequest request){
        String externalId = ExternalIdExtractor.getExternalIDSafely(id, request);
        groupService.updateGroupName(externalId, name, updateGroupNameDTO);
        return ResponseEntity.ok().build();
    }
}

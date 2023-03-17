package com.project.debby.domain.user.controller;

import com.project.debby.domain.integrations.minio.service.MinioService;
import com.project.debby.domain.user.dto.request.UpdateUserDTO;
import com.project.debby.domain.user.dto.request.UserRegisterDTO;
import com.project.debby.domain.user.dto.response.NotificationDTO;
import com.project.debby.domain.user.dto.response.UserWithAvatarDTO;
import com.project.debby.domain.user.model.Notification;
import com.project.debby.domain.user.model.User;
import com.project.debby.domain.user.service.NotificationService;
import com.project.debby.domain.user.service.UserService;
import com.project.debby.util.service.request.ExternalIdExtractor;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Tag(name = "user", description = "The user API")
@RestController
@RequestMapping("/users")
public class  UserController {
    private final NotificationService notificationService;
    private final UserService userService;
    private final MinioService minioService;

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserDTO updateDTO, @PathVariable String id, HttpServletRequest request){
        String externalID = ExternalIdExtractor.getExternalIDSafely(id, request);
        log.info("Started: update user | user extID {}", externalID);
        userService.updateUser(updateDTO, externalID);
        log.info("Complete: update user | user extID {}", externalID);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PutMapping("/{id}/avatar")
    ResponseEntity<UserWithAvatarDTO> updateAvatar(@PathVariable String id, @RequestParam("file") MultipartFile avatarFile,
                                                          HttpServletRequest request) {
        String externalID = ExternalIdExtractor.getExternalIDSafely(id, request);
        log.info("Started: update avatar | user extID {}", externalID);
        User user = userService.updateAvatar(avatarFile, externalID);
        log.info("Complete: update avatar | user extID {}", externalID);
        return ResponseEntity.ok(UserWithAvatarDTO.create(user, minioService.getAvatarURL(user)));
    }

    @SneakyThrows
    @PostMapping("/sign-up")
    public ResponseEntity<Void> createUser(@RequestBody UserRegisterDTO registerDTO){
        log.info("Started: sign-up | user email {}", registerDTO.getEmail());
        userService.registerUser(registerDTO);
        log.info("Complete: sign-up | user email {}", registerDTO.getEmail());
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<UserWithAvatarDTO> getUser(@PathVariable String id){
        log.info("Started: select user | user extId {} ", id);
        User user = userService.getUser(id);
        log.info("Complete: select user | user extId {} ", id);
        return ResponseEntity.ok(UserWithAvatarDTO.create(user, minioService.getAvatarURL(user)));
    }

    @SneakyThrows
    @GetMapping("/{id}/notifications")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable String id, HttpServletRequest request){
        String externalID = ExternalIdExtractor.getExternalIDSafely(id, request);
        List<Notification> notifications = notificationService.getAllNotifications(externalID);
        return ResponseEntity.ok(notifications.stream().map(NotificationDTO::create).collect(Collectors.toList()));
    }

    @SneakyThrows
    @GetMapping
    public ResponseEntity<UserWithAvatarDTO> getByEmail(@RequestParam String email){
        log.info("Started: selecting by email {}", email);
        User user = userService.getByEmail(email);
        return ResponseEntity.ok(UserWithAvatarDTO.create(user, minioService.getAvatarURL(user)));
    }

    @SneakyThrows
    @GetMapping
    public ResponseEntity<List<UserWithAvatarDTO>> getByUsername(@RequestParam("username") String username){
        log.info("Started: selecting by username {}", username);
        List<User> users = userService.getByUsername(username);
        return ResponseEntity.ok(users.stream()
                .map((v) -> UserWithAvatarDTO.create(v, minioService.getAvatarURL(v)))
                .collect(Collectors.toList()));
    }
}

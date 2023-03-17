package com.project.debby.domain.user.controller;

import com.project.debby.domain.auth.dto.request.LoginDTO;
import com.project.debby.domain.auth.dto.response.TokenDTO;
import com.project.debby.domain.auth.model.Token;
import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.domain.auth.service.ConfirmationService;
import com.project.debby.domain.auth.service.TokenService;
import com.project.debby.domain.auth.service.UserDetailsService;
import com.project.debby.domain.integrations.minio.service.MinioService;
import com.project.debby.domain.user.dto.request.*;
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
@RequestMapping("/user")
public class  UserController {

    private final ConfirmationService confirmationService;
    private final UserDetailsService userDetailsService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final TokenService tokenService;
    private final MinioService minioService;

    @SneakyThrows
    @PutMapping("/name")
    public ResponseEntity<Void> updateName(@RequestBody UsernameUpdateDTO nameDTO, HttpServletRequest request){
        String externalID = ExternalIdExtractor.getExternalID(request);
        log.info("Started: update name | user extID {}", externalID);
        userService.updateUsername(nameDTO, externalID);
        log.info("Complete: update name | user extID {}", externalID);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PutMapping("/avatar")
    ResponseEntity<UserWithAvatarDTO> updateAvatar(@RequestParam("file") MultipartFile avatarFile,
                                                          HttpServletRequest request) {
        String externalID = ExternalIdExtractor.getExternalID(request);
        log.info("Started: update avatar | user extID {}", externalID);
        User user = userService.updateAvatar(avatarFile, externalID);
        log.info("Complete: update avatar | user extID {}", externalID);
        return ResponseEntity.ok(UserWithAvatarDTO.create(user, minioService.getAvatarURL(user)));
    }

    @SneakyThrows
    @PutMapping("/email")
    public ResponseEntity<Void> updateUserEmail(@RequestBody ChangeEmailDTO emailDTO, HttpServletRequest request){
        String externalID = ExternalIdExtractor.getExternalID(request);
        log.info("Started: update email | user extID {}", externalID);
        userDetailsService.changeEmail(emailDTO, externalID);
        log.info("Complete: update email | user extID {}", externalID);
        confirmationService.sendConfirmation(emailDTO.getNewEmail(), externalID);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirm/{hash}")
    public ResponseEntity<TokenDTO> enableUser(@PathVariable String hash){
        log.info("Started: enabling user");
        UserDetails userDetails = confirmationService.checkConfirmation(hash);
        log.info("Complete: enabling user | user extId {}", userDetails.getCredentials().getExternalId());

        log.info("Started: generating tokens | user extId {}", userDetails.getCredentials().getExternalId());
        Token token = tokenService.createToken(userDetails);
        log.info("Complete: generating tokens | user extId {}", userDetails.getCredentials().getExternalId());
        return ResponseEntity.ok(TokenDTO.create(token));
    }

    @SneakyThrows
    @PostMapping("/sign-in")
    public ResponseEntity<TokenDTO> signIn(@RequestBody LoginDTO loginDTO){
        log.info("Started: sign-in | user email {}", loginDTO.getEmail());
        UserDetails userDetails = userDetailsService.signIn(loginDTO);
        log.info("Complete: sign-in | user email {}", loginDTO.getEmail());

        log.info("Started: generating tokens | user extId {}", userDetails.getCredentials().getExternalId());
        Token token = tokenService.createToken(userDetails);
        log.info("Complete: generating tokens | user extId {}", userDetails.getCredentials().getExternalId());
        return ResponseEntity.ok(TokenDTO.create(token));
    }

    @SneakyThrows
    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody UserRegisterDTO registerDTO){
        log.info("Started: sign-up | user email {}", registerDTO.getEmail());
        userService.registerUser(registerDTO);
        log.info("Complete: sign-up | user email {}", registerDTO.getEmail());
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/refresh")
    public ResponseEntity<TokenDTO> refreshToken(@RequestBody String refreshToken){
        log.info("Started: refreshing tokens");
        Token token = tokenService.refreshToken(refreshToken);
        log.info("Complete: refreshing tokens");
        return ResponseEntity.ok(TokenDTO.create(token));
    }

    @SneakyThrows
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String refreshToken, HttpServletRequest request){
        log.info("Started: logout | user extId {} ", ExternalIdExtractor.getExternalID(request));
        tokenService.deleteToken(refreshToken);
        log.info("Complete: logout | user extId {} ", ExternalIdExtractor.getExternalID(request));
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping("/get/by-id/{externalId}")
    public ResponseEntity<UserWithAvatarDTO> getUser(@PathVariable String externalId){
        log.info("Started: select user | user extId {} ", externalId);
        User user = userService.getUser(externalId);
        log.info("Complete: select user | user extId {} ", externalId);
        return ResponseEntity.ok(UserWithAvatarDTO.create(user, minioService.getAvatarURL(user)));
    }

    @SneakyThrows
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordUpdateDTO updateDTO, HttpServletRequest request){
        String externalID = ExternalIdExtractor.getExternalID(request);
        log.info("Started: changing password | user extId {} ", externalID);
        userDetailsService.updatePassword(updateDTO, externalID);
        log.info("Complete: changing password | user extId {} ", externalID);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDTO>> getNotifications(HttpServletRequest request){
        String externalID = ExternalIdExtractor.getExternalID(request);
        List<Notification> notifications = notificationService.getAllNotifications(externalID);
        return ResponseEntity.ok(notifications.stream().map(NotificationDTO::create).collect(Collectors.toList()));
    }

    @SneakyThrows
    @GetMapping("/me")
    public ResponseEntity<UserWithAvatarDTO> getMe(HttpServletRequest request){
        String externalID = ExternalIdExtractor.getExternalID(request);
        User user = userService.getUser(externalID);
        return ResponseEntity.ok(UserWithAvatarDTO.create(user, minioService.getAvatarURL(user)));
    }

    @SneakyThrows
    @GetMapping("/get/by-email")
    public ResponseEntity<UserWithAvatarDTO> getByEmail(@RequestParam String email){
        log.info("Started: selecting by email {}", email);
        User user = userService.getByEmail(email);
        return ResponseEntity.ok(UserWithAvatarDTO.create(user, minioService.getAvatarURL(user)));
    }

    @SneakyThrows
    @GetMapping("/get/by-username")
    public ResponseEntity<List<UserWithAvatarDTO>> getByUsername(@RequestParam("username") String username){
        log.info("Started: selecting by username {}", username);
        List<User> users = userService.getByUsername(username);
        return ResponseEntity.ok(users.stream()
                .map((v) -> UserWithAvatarDTO.create(v, minioService.getAvatarURL(v)))
                .collect(Collectors.toList()));
    }

    @SneakyThrows
    @PostMapping("/user-id")
    public ResponseEntity<TokenDTO> updateUserID(@RequestBody UpdateExternalID updateDTO, HttpServletRequest request){
        String externalID = ExternalIdExtractor.getExternalID(request);
        User user = userService.updateExternalID(updateDTO, externalID);
        Token token = tokenService.createToken(user.getUserDetails());
        return ResponseEntity.ok(TokenDTO.create(token));
    }
}

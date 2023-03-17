package com.project.debby.domain.auth.controller;

import com.project.debby.domain.auth.dto.request.LoginDTO;
import com.project.debby.domain.auth.dto.response.TokenDTO;
import com.project.debby.domain.auth.model.Token;
import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.domain.auth.service.ConfirmationService;
import com.project.debby.domain.auth.service.TokenService;
import com.project.debby.domain.auth.service.UserDetailsService;
import com.project.debby.domain.user.dto.request.ChangeEmailDTO;
import com.project.debby.domain.user.dto.request.PasswordUpdateDTO;
import com.project.debby.util.service.JwtService;
import com.project.debby.util.service.request.ExternalIdExtractor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserDetailsService userDetailsService;
    private final ConfirmationService confirmationService;
    private final TokenService tokenService;
    private final JwtService jwtService;

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
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String refreshToken, HttpServletRequest request){
        log.info("Started: logout | user extId {} ", ExternalIdExtractor.getExternalIDSafely(
                jwtService.extractUserIdFromToken(refreshToken), request));
        tokenService.deleteToken(refreshToken);
        log.info("Complete: logout | user extId {} ", ExternalIdExtractor.getExternalIDSafely(
                jwtService.extractUserIdFromToken(refreshToken),request));
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
    @PutMapping("/{id}/email")
    public ResponseEntity<Void> updateUserEmail(@PathVariable String id, @RequestBody ChangeEmailDTO emailDTO, HttpServletRequest request){
        String externalID = ExternalIdExtractor.getExternalIDSafely(id, request);
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
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable String id, @RequestBody PasswordUpdateDTO updateDTO, HttpServletRequest request){
        String externalID = ExternalIdExtractor.getExternalIDSafely(id, request);
        log.info("Started: changing password | user extId {} ", externalID);
        userDetailsService.updatePassword(updateDTO, externalID);
        log.info("Complete: changing password | user extId {} ", externalID);
        return ResponseEntity.ok().build();
    }
}

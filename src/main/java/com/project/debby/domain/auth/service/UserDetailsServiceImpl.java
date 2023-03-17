package com.project.debby.domain.auth.service;

import com.project.debby.configuration.security.providers.exceptions.UserDisabledException;
import com.project.debby.configuration.security.providers.exceptions.UserExpiredException;
import com.project.debby.configuration.security.providers.exceptions.UserLockedException;
import com.project.debby.domain.auth.dto.request.LoginDTO;
import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.domain.auth.model.repository.UserDetailsRepository;
import com.project.debby.domain.auth.service.exceptions.PasswordDoNotMatchesException;
import com.project.debby.domain.user.dto.request.ChangeEmailDTO;
import com.project.debby.domain.user.dto.request.PasswordUpdateDTO;
import com.project.debby.domain.user.service.exception.UserAlreadyExistException;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(PasswordUpdateDTO passwordUpdateDTO, String UserID) throws RequestedEntityNotFound, PasswordDoNotMatchesException {
        log.debug("--changing password | user extId {}", UserID);
        UserDetails userDetails = getByExternalID(UserID);
        if(passwordEncoder.matches(passwordUpdateDTO.getOldPassword(), userDetails.getPassword())){
            userDetails.getCredentials().setPassword(
                    passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
            userDetailsRepository.saveAndFlush(userDetails);
        }
        else throw new PasswordDoNotMatchesException("User password not math. Attempt to change password denied.");
    }

    @Override
    public void changeEmail(ChangeEmailDTO updateDTO, String UserID) throws RequestedEntityNotFound,
            UserAlreadyExistException {
        Optional<UserDetails> checkUser = userDetailsRepository.findByCredentials_Email(updateDTO.getNewEmail());
        if (checkUser.isPresent()) throw new UserAlreadyExistException(
                "Failed to change email of user " + UserID +
                        ". User with email " + updateDTO.getNewEmail() + " already exists");
        log.debug("--updating email | user extId {}", UserID);
        UserDetails userDetails = getByExternalID(UserID);
        userDetails.getCredentials().setEmail(updateDTO.getNewEmail());
        userDetails.setEnabled(false);
        userDetailsRepository.saveAndFlush(userDetails);
    }

    @Override
    public void enableUser(String userID) throws RequestedEntityNotFound {
        log.debug("--enabling user | user extId {}", userID);
        UserDetails userDetails = getByExternalID(userID);
        userDetails.setEnabled(true);
        userDetailsRepository.saveAndFlush(userDetails);
    }

    @Override
    public UserDetails signIn(LoginDTO loginDTO) throws RequestedEntityNotFound, PasswordDoNotMatchesException {
        log.debug("--signing in | user email {}", loginDTO.getEmail());
        UserDetails user = userDetailsRepository.findByCredentials_Email(loginDTO.getEmail())
                .orElseThrow(
                        () -> new RequestedEntityNotFound("UserDetails with email " + loginDTO.getEmail() + "not found"
                        ));
        log.debug("--checking user| user email {}", loginDTO.getEmail());
        if (!user.isEnabled()){
            throw new UserDisabledException("User with email extracted from token is currently disabled.");
        }
        if (!user.isAccountNonExpired()){
            throw new UserExpiredException("User with email extracted from token is currently expired.");
        }
        if (!user.isAccountNonLocked()){
            throw new UserLockedException("User with email extracted from token is currently locked.");
        }
        log.debug("--checking password | user email {}", loginDTO.getEmail());
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getCredentials().getPassword())){
            throw new PasswordDoNotMatchesException("User password not math. Attempt to sign-in denied.");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsRepository.findByCredentials_ExternalId(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found."));
    }

    private UserDetails getByExternalID(String externalID) throws RequestedEntityNotFound {
        return userDetailsRepository.findByCredentials_ExternalId(externalID).orElseThrow(
                () -> new RequestedEntityNotFound("UserDetails with extId " + externalID + "not found"));
    }
}

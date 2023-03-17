package com.project.debby.domain.user.service;

import com.project.debby.domain.auth.service.ConfirmationService;
import com.project.debby.domain.integrations.minio.service.MinioService;
import com.project.debby.domain.user.dto.request.*;
import com.project.debby.domain.user.model.User;
import com.project.debby.domain.user.model.repository.UserRepository;
import com.project.debby.domain.user.service.exception.UserAlreadyExistException;
import com.project.debby.domain.user.service.factory.UserFactory;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final ConfirmationService confirmationService;
    private final UserRepository userRepository;
    private final MinioService minioService;
    private final UserFactory userFactory;

    @Override
    public User getUser(String externalID) throws RequestedEntityNotFound {
        log.info(externalID);
        return userRepository.findByUserDetails_Credentials_ExternalId(externalID)
                .orElseThrow(() -> new RequestedEntityNotFound("User with extId" + externalID + " not found"));
    }

    @Override
    public synchronized void registerUser(UserRegisterDTO registerDTO) throws UserAlreadyExistException {
        log.debug("--creating new user | email {}", registerDTO.getEmail());
        Optional<User> checkUser = userRepository.findByUserDetails_Credentials_Email(registerDTO.getEmail());
        if (checkUser.isPresent()) throw new UserAlreadyExistException("User with email " +
                registerDTO.getEmail() + " already exist.");
        User user = userFactory.create(registerDTO);
        confirmationService.sendConfirmation(registerDTO.getEmail(), user.getUserDetails().getUsername());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUser(UpdateUserDTO updateDTO, String userID) throws RequestedEntityNotFound, UserAlreadyExistException {
        log.debug("--updating user | user extId {}", userID);
        User user = getUser(userID);
        user.setUsername(updateDTO.getUsername());
        if (updateDTO.getExternalId() != null
                && !updateDTO.getExternalId().equals("")
                && !user.getUserDetails().getCredentials().getExternalId().equals(updateDTO.getExternalId())){
            updateExternalID(updateDTO.getExternalId(), user.getUserDetails().getCredentials().getExternalId());
        }
        userRepository.saveAndFlush(user);
    }

    @Override
    public User updateAvatar(MultipartFile file, String userID) throws RequestedEntityNotFound {
        log.debug("--updating user avatar | user extId {}", userID);
        User user = getUser(userID);
        user.setAvatar(minioService.saveUserAvatar(user, file));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void updateSettings(SettingsUpdateDTO updateDTO, String userID) throws RequestedEntityNotFound {
        log.debug("--updating user settings | user extId {}", userID);
        User user = getUser(userID);
        user.getSettings().setEmailNotificationEnabled(updateDTO.isEmailNotificationEnabled());
        user.getSettings().setPushNotificationEnabled(updateDTO.isPushNotificationEnabled());
        userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> getByUsername(String username) {
        return userRepository.getAllByUsernameContains(username);
    }

    @Override
    public User getByEmail(String email) throws RequestedEntityNotFound {
        return userRepository.findByUserDetails_Credentials_Email(email)
                .orElseThrow(() -> new RequestedEntityNotFound("User with email " + email + " not found"));
    }

    @Override
    public synchronized void updateExternalID(String updatedExternalId, String userID) throws RequestedEntityNotFound, UserAlreadyExistException {
        log.debug("--updating user externalId | user extId {}, new Id {}", userID, updatedExternalId);
        Optional<User> checkUser = userRepository
                .findByUserDetails_Credentials_ExternalId(updatedExternalId);
        if (checkUser.isPresent() || updatedExternalId.equals("me"))
            throw new UserAlreadyExistException("External id change failed. User with extId" +
                    updatedExternalId + " already exist");
        User user = getUser(userID);
        user.getUserDetails().getCredentials().setExternalId(updatedExternalId);
        userRepository.saveAndFlush(user);
    }
}

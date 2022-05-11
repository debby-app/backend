package com.project.debby.domain.user.service;

import com.project.debby.domain.auth.service.ConfirmationService;
import com.project.debby.domain.integrations.minio.service.MinioService;
import com.project.debby.domain.user.dto.request.SettingsUpdateDTO;
import com.project.debby.domain.user.dto.request.UserRegisterDTO;
import com.project.debby.domain.user.dto.request.UsernameUpdateDTO;
import com.project.debby.domain.user.model.User;
import com.project.debby.domain.user.model.repository.UserRepository;
import com.project.debby.domain.user.service.factory.UserFactory;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
                .orElseThrow(RequestedEntityNotFound::new);
    }

    @Override
    public User registerUser(UserRegisterDTO registerDTO) {
        User user = userFactory.create(registerDTO);
        confirmationService.sendConfirmation(registerDTO.getEmail(), user.getUserDetails().getUsername());
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUsername(UsernameUpdateDTO updateDTO, String userID) throws RequestedEntityNotFound {
        User user = getUser(userID);
        user.setUsername(updateDTO.getNewUsername());
        userRepository.saveAndFlush(user);
    }

    @Override
    public User updateAvatar(MultipartFile file, String userID) throws RequestedEntityNotFound {
        User user = getUser(userID);
        user.setAvatar(minioService.saveUserAvatar(user, file));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void updateSettings(SettingsUpdateDTO updateDTO, String userID) throws RequestedEntityNotFound {
        User user = getUser(userID);
        user.getSettings().setEmailNotificationEnabled(updateDTO.isEmailNotificationEnabled());
        user.getSettings().setPushNotificationEnabled(updateDTO.isPushNotificationEnabled());
        userRepository.saveAndFlush(user);
    }
}

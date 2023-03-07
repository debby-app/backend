package com.project.debby.domain.user.service;

import com.project.debby.domain.user.dto.request.*;
import com.project.debby.domain.user.model.User;
import com.project.debby.domain.user.service.exception.UserAlreadyExistException;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User getUser(String externalID) throws RequestedEntityNotFound;
    User registerUser(UserRegisterDTO registerDTO) throws UserAlreadyExistException;
    void updateUsername(UsernameUpdateDTO updateDTO, String UserID) throws RequestedEntityNotFound;
    User updateAvatar(MultipartFile file, String userID) throws RequestedEntityNotFound;
    void updateSettings(SettingsUpdateDTO updateDTO, String UserId) throws RequestedEntityNotFound;
    List<User> getByUsername(String username);
    User getByEmail(String email) throws RequestedEntityNotFound;
    User updateExternalID(UpdateExternalID updateDTO, String userID) throws RequestedEntityNotFound, UserAlreadyExistException;
}

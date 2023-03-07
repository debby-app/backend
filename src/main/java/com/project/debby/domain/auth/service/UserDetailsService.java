package com.project.debby.domain.auth.service;

import com.project.debby.domain.auth.dto.request.LoginDTO;
import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.domain.auth.service.exceptions.PasswordDoNotMatchesException;
import com.project.debby.domain.user.dto.request.ChangeEmailDTO;
import com.project.debby.domain.user.dto.request.PasswordUpdateDTO;
import com.project.debby.domain.user.service.exception.UserAlreadyExistException;
import com.project.debby.util.exceptions.RequestedEntityNotFound;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    void updatePassword(PasswordUpdateDTO passwordUpdateDTO, String UserID) throws RequestedEntityNotFound, PasswordDoNotMatchesException;
    void changeEmail(ChangeEmailDTO updateDTO, String UserID) throws RequestedEntityNotFound, UserAlreadyExistException;
    void enableUser(String userID) throws RequestedEntityNotFound;
    UserDetails signIn(LoginDTO loginDTO) throws RequestedEntityNotFound, PasswordDoNotMatchesException;
}

package com.project.debby.domain.auth.service;

import com.project.debby.domain.auth.model.ConfirmationMail;
import com.project.debby.domain.auth.model.UserDetails;

public interface ConfirmationService {
    ConfirmationMail sendConfirmation(String email, String externalID);
    UserDetails checkConfirmation(String hash);
}

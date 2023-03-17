package com.project.debby.domain.auth.service;

import com.project.debby.domain.auth.model.UserDetails;

public interface ConfirmationService {
    void sendConfirmation(String email, String externalID);
    UserDetails checkConfirmation(String hash);
}

package com.project.debby.configuration.security.providers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED, reason = "Account currently disabled. Verify email.")
public class UserDisabledException extends AuthenticationException {
    public UserDisabledException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserDisabledException(String msg) {
        super(msg);
    }
}

package com.project.debby.configuration.security.providers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED, reason = "Account is locked.")
public class UserLockedException extends AuthenticationException {
    public UserLockedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserLockedException(String msg) {
        super(msg);
    }
}

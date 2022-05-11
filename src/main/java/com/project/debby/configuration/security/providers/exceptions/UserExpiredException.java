package com.project.debby.configuration.security.providers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Account expired")
public class UserExpiredException extends AuthenticationException {
    public UserExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserExpiredException(String msg) {
        super(msg);
    }
}

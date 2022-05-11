package com.project.debby.configuration.security.providers;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.HashSet;

public class JWTPreAuthenticationToken extends AbstractAuthenticationToken {

    private final String userId;

    public JWTPreAuthenticationToken(String userId) {
        super(new HashSet<>());
        this.userId = userId;
    }

    @Override
    public Object getCredentials() {
        return "JWT pre auth";
    }

    @Override
    public Object getPrincipal() {
        return "JWT pre auth";
    }

    @Override
    public String getName() {
        return userId;
    }
}

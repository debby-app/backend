package com.project.debby.configuration.security.providers;

import com.project.debby.domain.auth.model.Credentials;
import com.project.debby.domain.auth.model.UserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails principal;
    private final Credentials credentials;

    public JWTAuthenticationToken(UserDetails principal, Credentials credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

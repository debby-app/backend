package com.project.debby.configuration.security.providers;

import com.project.debby.configuration.security.providers.exceptions.UserDisabledException;
import com.project.debby.configuration.security.providers.exceptions.UserExpiredException;
import com.project.debby.configuration.security.providers.exceptions.UserIsMissedException;
import com.project.debby.configuration.security.providers.exceptions.UserLockedException;
import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.domain.auth.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class JWTAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = authentication.getName();
        UserDetails user;
        try{
            user = (UserDetails) userDetailsService.loadUserByUsername(userId);
            if (!user.isEnabled()){
                throw new UserDisabledException("User with id extracted from token is currently disabled.");
            }
            if (!user.isAccountNonExpired()){
                throw new UserExpiredException("User with id extracted from token is currently expired.");
            }
            if (!user.isAccountNonLocked()){
                throw new UserLockedException("User with id extracted from token is currently locked.");
            }
        }
        catch (UsernameNotFoundException ex){
            throw new UserIsMissedException("User with id extracted from token does not exist", ex);
        }

        JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(user, user.getCredentials(),
                user.getAuthorities());

        jwtAuthenticationToken.setAuthenticated(true);

        return jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}

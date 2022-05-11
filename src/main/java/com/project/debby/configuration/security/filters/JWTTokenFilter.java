package com.project.debby.configuration.security.filters;

import com.project.debby.configuration.security.providers.JWTAuthenticationProvider;
import com.project.debby.configuration.security.providers.JWTPreAuthenticationToken;
import com.project.debby.util.service.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.ObjectUtils.isEmpty;

@Log4j2
@RequiredArgsConstructor
@Component
public class JWTTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JWTAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        final String token = header.split(" ")[1].trim();

        if (jwtService.isTokenExpired(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        final String userId = jwtService.extractUserIdFromToken(token);
        httpServletRequest.setAttribute("UserID", userId);

        JWTPreAuthenticationToken preAuth = new JWTPreAuthenticationToken(userId);
        Authentication authentication = jwtAuthenticationProvider.authenticate(preAuth);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}

package com.example.onlinecourses.filters;

import com.example.onlinecourses.providers.JwtProvider;
import com.example.onlinecourses.utils.RequestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class CookieFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        Optional<String> refreshTokenOptional = RequestUtil.extractCookieFromRequest(request, "refreshToken");

        if (refreshTokenOptional.isPresent()) {
            String refreshToken = refreshTokenOptional.get();
            // Validate the token and set the authentication in the SecurityContext
            if (JwtProvider.validateToken(refreshToken)) {
                Authentication authentication = JwtProvider.getAuthentication(refreshToken);
                ((AbstractAuthenticationToken) authentication).setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}

package com.example.onlinecourses.filters;

import com.example.onlinecourses.backgroundJobs.interfaces.ITokenBlacklistService;
import com.example.onlinecourses.filters.abstracts.BaseTokenFilter;
import com.example.onlinecourses.services.interfaces.IUserService;
import com.example.onlinecourses.utils.RequestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

import java.io.IOException;
import java.util.Optional;

public class CookieFilter extends BaseTokenFilter {


    public CookieFilter(ITokenBlacklistService tokenBlacklistService, IUserService userService) {
        super(userService, tokenBlacklistService);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if(super.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        Optional<String> refreshTokenOptional = RequestUtil.extractCookieFromRequest(request, "refreshToken");

        if (refreshTokenOptional.isPresent()) {
            String refreshToken = refreshTokenOptional.get();
            super.authenticateToken(refreshToken);
        }
        filterChain.doFilter(request, response);
    }
}

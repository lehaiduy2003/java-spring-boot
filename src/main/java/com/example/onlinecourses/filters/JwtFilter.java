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

public class JwtFilter extends BaseTokenFilter {


    public JwtFilter(ITokenBlacklistService tokenBlacklistService, IUserService userService) {
        super(userService, tokenBlacklistService);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        if(super.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> tokenOptional = RequestUtil.extractJwtFromRequest(request);
        if (tokenOptional.isPresent()) {
            String token = tokenOptional.get();
            super.authenticateToken(token);
        }
        filterChain.doFilter(request, response);
    }

}

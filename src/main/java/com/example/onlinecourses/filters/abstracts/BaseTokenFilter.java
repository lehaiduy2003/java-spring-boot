package com.example.onlinecourses.filters.abstracts;

import com.example.onlinecourses.backgroundJobs.interfaces.ITokenBlacklistService;
import com.example.onlinecourses.configs.impls.UserDetailsImpl;
import com.example.onlinecourses.services.interfaces.IUserService;
import com.example.onlinecourses.utils.JwtUtil;
import com.example.onlinecourses.utils.SecurityContextUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;


// This class is an abstract class that extends OncePerRequestFilter
// This class is used to filter the token
// Child class must implement the doFilterInternal method
public abstract class BaseTokenFilter extends OncePerRequestFilter {
    private final IUserService userService;
    private final ITokenBlacklistService tokenBlacklistService;

    protected BaseTokenFilter(IUserService userService, ITokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    private void handleValidToken(String token) {
        Claims claims = JwtUtil.extractClaims(token);
        String email = claims.getSubject();
        UserDetailsImpl userDetails =(UserDetailsImpl) userService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextUtil.setSecurityContext(authentication);
    }

    protected void authenticateToken(String token) {
        if(tokenBlacklistService.isRevoked(token)) {
            throw new MalformedJwtException("Token is revoked");
        }
        // Validate the token and set the attribute if it is valid
        if (JwtUtil.validateToken(token)) {
            this.handleValidToken(token);
        }
    }
}

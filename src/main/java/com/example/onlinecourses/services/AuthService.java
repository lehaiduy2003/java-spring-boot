package com.example.onlinecourses.services;

import com.example.onlinecourses.configs.impls.UserDetailsImpl;
import com.example.onlinecourses.dtos.auth.AuthRequestDTO;
import com.example.onlinecourses.dtos.auth.AuthResponseDTO;
import com.example.onlinecourses.dtos.requests.post.UserCreationDTO;
import com.example.onlinecourses.dtos.models.UserDTO;
import com.example.onlinecourses.mappers.UserMapper;
import com.example.onlinecourses.backgroundJobs.interfaces.ITokenBlacklistService;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.services.interfaces.IUserService;
import com.example.onlinecourses.utils.JwtUtil;
import com.example.onlinecourses.services.interfaces.IAuthService;
import com.example.onlinecourses.utils.SecurityContextUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final IUserService userService;
    private final AuthenticationProvider authenticationProvider;
    private final ITokenBlacklistService tokenBlacklistService;
    private final PasswordEncoder passwordEncoder;

    // Authenticate the user
    private Authentication authenticate(String email, String password) {
        return authenticationProvider.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
    }

    private User toUser(UserDetailsImpl userDetails) {
        return UserMapper.INSTANCE.toEntity(userDetails);
    }

    @Override
    public AuthResponseDTO signIn(AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticate(authRequestDTO.getEmail(), authRequestDTO.getPassword());
        User user = toUser((UserDetailsImpl) authentication.getPrincipal());
        String accessToken = JwtUtil.generateAccessToken(user);
        String refreshToken = JwtUtil.generateRefreshToken(user);

        return AuthResponseDTO.builder().user(UserMapper.INSTANCE.toUserDataDTO(user)).accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public AuthResponseDTO signUp(UserCreationDTO userCreationDTO) {
        String originalPassword = userCreationDTO.getPassword();
        String hashedPassword = passwordEncoder.encode(userCreationDTO.getPassword());
        userCreationDTO.setPassword(hashedPassword);
        UserDTO userDTO = userService.create(userCreationDTO);
        // auto login after sign up
        Authentication authentication = authenticate(userDTO.getEmail(), originalPassword);
        User user = toUser((UserDetailsImpl) authentication.getPrincipal());
        String accessToken = JwtUtil.generateAccessToken(user);
        String refreshToken = JwtUtil.generateRefreshToken(user);

        return AuthResponseDTO.builder().user(UserMapper.INSTANCE.toUserDataDTO(userDTO)).accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        // Extract the token to get subject (userId)
       Claims claims = JwtUtil.extractClaims(refreshToken);
       if(claims == null) {
           throw new IllegalArgumentException("Invalid refresh token");
       }
       if(claims.getExpiration().before(new Date())) {
           throw new ExpiredJwtException(null, claims, "Refresh token has expired");
       }
       // get the authentication object from the security context
       Authentication authentication = SecurityContextUtil.getAuthentication();
       User user = toUser((UserDetailsImpl) authentication.getPrincipal());
       return JwtUtil.generateAccessToken(user); // Generate a new access token
    }

    @Override
    public void signOut(String refreshToken) {
        Claims claims = JwtUtil.extractClaims(refreshToken);

        long expiration = (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000; // Get the remaining time in seconds

        if(expiration > 0) {
            tokenBlacklistService.revokeToken(refreshToken, expiration);
        } else {
            throw new ExpiredJwtException(null, claims, "Refresh token has expired");
        }
    }

}

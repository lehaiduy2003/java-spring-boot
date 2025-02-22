package com.example.onlinecourses.services;

import com.example.onlinecourses.dtos.auth.AuthRequestDTO;
import com.example.onlinecourses.dtos.auth.AuthResponseDTO;
import com.example.onlinecourses.dtos.reqMethod.post.UserCreationDTO;
import com.example.onlinecourses.dtos.models.UserDTO;
import com.example.onlinecourses.providers.JwtProvider;
import com.example.onlinecourses.services.Interfaces.IAuthService;
import com.example.onlinecourses.specifications.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationProvider authenticationProvider;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserService userService, JwtProvider jwtProvider, AuthenticationProvider authenticationProvider) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public AuthResponseDTO signIn(AuthRequestDTO authRequestDTO) {
        UserDTO userDTO  = userService.findByEmail(authRequestDTO.getEmail());
        if(passwordEncoder.matches(authRequestDTO.getPassword(), userDTO.getPassword())) {
            Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword())
            );

            setSecurityContext(authentication);

            String accessToken = jwtProvider.generateAccessToken(authentication);
            String refreshToken = jwtProvider.generateRefreshToken(authentication);

            return AuthResponseDTO.builder().userId(userDTO.getId()).accessToken(accessToken).refreshToken(refreshToken).build();

        } else {
            throw new RuntimeException("Invalid password");
        }
    }

    @Override
    public AuthResponseDTO signUp(UserCreationDTO userCreationDTO) throws IllegalArgumentException {
        String hashedPassword = passwordEncoder.encode(userCreationDTO.getPassword());
        userCreationDTO.setPassword(hashedPassword);
        UserDTO userDTO = userService.create(userCreationDTO);
        // Create a UserDetailsImpl object from the userDTO object
        UserDetailsImpl userDetails = (UserDetailsImpl) userService.loadUserByUsername(userDTO.getEmail());
        // Create an authentication object using the UserDetailsImpl object
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, // Use the UserDetailsImpl object as the principal
            null, // No need for credentials
            userDetails.getAuthorities() // Get the authorities from the UserDetailsImpl object
        );
        setSecurityContext(authentication);
        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        return AuthResponseDTO.builder().userId(userDTO.getId()).accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
       return jwtProvider.refreshAccessToken(refreshToken);
    }

    @Override
    public void signOut(String refreshToken) {

    }

    private void setSecurityContext(Authentication authentication) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}

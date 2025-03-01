package com.example.onlinecourses.controllers;
import com.example.onlinecourses.dtos.responses.ApiResponse;
import com.example.onlinecourses.dtos.auth.AuthRequestDTO;
import com.example.onlinecourses.dtos.auth.AuthResponseDTO;
import com.example.onlinecourses.dtos.requests.post.UserCreationDTO;
import com.example.onlinecourses.services.interfaces.IAuthService;
import com.example.onlinecourses.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IAuthService authService;
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> signIn(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response, HttpSession httpSession) {
        AuthResponseDTO authResponseDTO = authService.signIn(authRequestDTO);
        // Set session attribute
        httpSession.setAttribute("userId", authResponseDTO.getUser().getId());
        CookieUtil.setCookies(response, REFRESH_TOKEN_COOKIE_NAME, authResponseDTO.getRefreshToken());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Sign in successful", authResponseDTO));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> signUp(@Valid @RequestBody UserCreationDTO userCreationDTO, HttpServletResponse response, HttpSession httpSession) {
        AuthResponseDTO authResponseDTO = authService.signUp(userCreationDTO);
        // Set session attribute
        httpSession.setAttribute("userId", authResponseDTO.getUser().getId());
        CookieUtil.setCookies(response, REFRESH_TOKEN_COOKIE_NAME, authResponseDTO.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Sign up successful", authResponseDTO));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<ApiResponse<Object>> signOut(@RequestAttribute(name = REFRESH_TOKEN_COOKIE_NAME) String refreshToken, HttpServletResponse response) {
        // Delete refresh token cookie
        authService.signOut(refreshToken);
        CookieUtil.deleteCookies(response, REFRESH_TOKEN_COOKIE_NAME);
        return ResponseEntity.ok(new ApiResponse<>(true, "Sign out successful", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, String>>> refreshToken(
        @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken) {
        Map<String, String> responseBody = new HashMap<>();
        if (refreshToken == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Refresh token not found", null));
        }
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        responseBody.put("accessToken", newAccessToken);
        return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", responseBody));
    }

}
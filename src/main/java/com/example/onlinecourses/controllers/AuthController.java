package com.example.onlinecourses.controllers;
import com.example.onlinecourses.dtos.ApiResponse;
import com.example.onlinecourses.dtos.auth.AuthRequestDTO;
import com.example.onlinecourses.dtos.auth.AuthResponseDTO;
import com.example.onlinecourses.dtos.reqMethod.post.UserCreationDTO;
import com.example.onlinecourses.services.Interfaces.IAuthService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IAuthService authService;
    private final boolean isSecure = Dotenv.load().get("ENV").equals("production");

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> signIn(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response, HttpSession httpSession) {
        try {
            AuthResponseDTO authResponseDTO = authService.signIn(authRequestDTO);

            // Set session attribute
            httpSession.setAttribute("userId", authResponseDTO.getUserId());

            setCookies(response, authResponseDTO.getRefreshToken());

            return ResponseEntity.ok(new ApiResponse<>(true, "Sign in successful", authResponseDTO));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> signUp(@Valid @RequestBody UserCreationDTO userCreationDTO, HttpServletResponse response, HttpSession httpSession) {
        try {
            AuthResponseDTO authResponseDTO = authService.signUp(userCreationDTO);

            // Set session attribute
            httpSession.setAttribute("userId", authResponseDTO.getUserId());

            setCookies(response, authResponseDTO.getRefreshToken());

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Sign up successful", authResponseDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/sign-out")
    public ResponseEntity<ApiResponse<?>> signOut(HttpServletResponse response) {
        // Xóa refresh token cookie
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/api/v1/auth/refresh");
        response.addCookie(cookie);

        return ResponseEntity.ok(new ApiResponse<>(true, "Sign out successful", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshToken(
        @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Refresh token not found", null));
        }

        try {
            String newAccessToken = authService.refreshAccessToken(refreshToken);

            return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", newAccessToken));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    private void setCookies(HttpServletResponse response, String refreshToken) {
        // Set refresh token cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(isSecure);
        refreshTokenCookie.setPath("/api/v1/auth/refresh"); // only send to /api/v1/auth/refresh endpoint
        response.addCookie(refreshTokenCookie);
    }
}
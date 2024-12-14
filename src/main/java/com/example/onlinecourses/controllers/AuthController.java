package com.example.onlinecourses.controllers;


import com.example.onlinecourses.dtos.AuthRequestDTO;
import com.example.onlinecourses.dtos.AuthResponseDTO;
import com.example.onlinecourses.services.Interfaces.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDTO> signIn(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response, HttpSession httpSession) {
        AuthResponseDTO authResponseDTO = authService.signIn(authRequestDTO);

        // Set cookies
        setCookies(response, authResponseDTO);

        // Set session attribute
        httpSession.setAttribute("userId", authResponseDTO.getUserId());
        return ResponseEntity.ok(authResponseDTO);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDTO> signUp(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response, HttpSession httpSession) {
        AuthResponseDTO authResponseDTO = authService.signUp(authRequestDTO);

        // Set cookies
        setCookies(response, authResponseDTO);

        // Set session attribute
        httpSession.setAttribute("userId", authResponseDTO.getUserId());
        return ResponseEntity.ok(authResponseDTO);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(HttpServletResponse response) {
        // Remove cookies
        removeCookies(response);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        AuthResponseDTO authResponseDTO = authService.refreshToken(refreshToken);

        // Set cookies
        setCookies(response, authResponseDTO);

        return ResponseEntity.ok(authResponseDTO);
    }

    private void setCookies(HttpServletResponse response, AuthResponseDTO authResponseDTO) {
        // Set access token cookie
        Cookie accessTokenCookie = new Cookie("accessToken", authResponseDTO.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        // Set refresh token cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", authResponseDTO.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        // Set user ID cookie
        Cookie userIdCookie = new Cookie("userId", String.valueOf(authResponseDTO.getUserId()));
        userIdCookie.setHttpOnly(true);
        userIdCookie.setPath("/");
        response.addCookie(userIdCookie);
    }

    private void removeCookies(HttpServletResponse response) {
        // Remove access token cookie
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        // Remove refresh token cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        // Remove user ID cookie
        Cookie userIdCookie = new Cookie("userId", null);
        userIdCookie.setHttpOnly(true);
        userIdCookie.setPath("/");
        userIdCookie.setMaxAge(0);
        response.addCookie(userIdCookie);
    }
}
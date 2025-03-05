package com.example.onlinecourses.controllers;
import com.example.onlinecourses.dtos.auth.RefreshResponseDTO;
import com.example.onlinecourses.dtos.responses.ApiResponseDTO;
import com.example.onlinecourses.dtos.auth.AuthRequestDTO;
import com.example.onlinecourses.dtos.auth.AuthResponseDTO;
import com.example.onlinecourses.dtos.requests.post.UserCreationDTO;
import com.example.onlinecourses.services.interfaces.IAuthService;
import com.example.onlinecourses.swagger.v1.AuthSwagger;
import com.example.onlinecourses.utils.CookieUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IAuthService authService;
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    @AuthSwagger.SignIn
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> signIn(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response, HttpSession httpSession) {
        AuthResponseDTO authResponseDTO = authService.signIn(authRequestDTO);
        // Set session attribute
        httpSession.setAttribute("userId", authResponseDTO.getUser().getId());
        CookieUtil.setCookies(response, REFRESH_TOKEN_COOKIE_NAME, authResponseDTO.getRefreshToken());
        return ResponseEntity.status(200).body(new ApiResponseDTO<>(true, "Sign in successful", authResponseDTO));
    }

    @PostMapping("/sign-up")
    @AuthSwagger.SignUp
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> signUp(@Valid UserCreationDTO userCreationDTO, HttpServletResponse response, HttpSession httpSession) {
        AuthResponseDTO authResponseDTO = authService.signUp(userCreationDTO);
        // Set session attribute
        httpSession.setAttribute("userId", authResponseDTO.getUser().getId());
        CookieUtil.setCookies(response, REFRESH_TOKEN_COOKIE_NAME, authResponseDTO.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, "Sign up successful", authResponseDTO));
    }

    @PostMapping("/sign-out")
    @AuthSwagger.SignOut
    public ResponseEntity<ApiResponseDTO<Nullable>> signOut(@RequestAttribute(name = REFRESH_TOKEN_COOKIE_NAME) String refreshToken, HttpServletResponse response) {
        // revoke the refresh token
        authService.signOut(refreshToken);
        CookieUtil.deleteCookies(response, REFRESH_TOKEN_COOKIE_NAME);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Sign out successful", null));
    }

    @PostMapping("/refresh")
    @AuthSwagger.RefreshAccessToken
    public ResponseEntity<ApiResponseDTO<RefreshResponseDTO>> refreshToken(
        @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(false, "Refresh token not found", null));
        }
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        RefreshResponseDTO refreshResponse = RefreshResponseDTO.builder().accessToken(newAccessToken).build();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Token refreshed successfully", refreshResponse));
    }

}
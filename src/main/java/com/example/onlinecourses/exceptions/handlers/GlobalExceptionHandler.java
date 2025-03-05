package com.example.onlinecourses.exceptions.handlers;

import com.example.onlinecourses.dtos.responses.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class is used to handle global exceptions (500)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleException(Exception e) {
        return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e) {
        return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleOAuth2AuthenticationException(OAuth2AuthenticationException e) {
        return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }
}

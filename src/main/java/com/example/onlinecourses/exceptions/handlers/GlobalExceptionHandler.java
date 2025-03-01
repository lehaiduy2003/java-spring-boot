package com.example.onlinecourses.exceptions.handlers;

import com.example.onlinecourses.dtos.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class is used to handle global exceptions (500)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

//    // Handle uncaught exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Exception>> handleException(Exception e) {
//        return ResponseEntity.status(500).body(new ApiResponse<>(false, e.getMessage(), e));
//    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Exception>> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.status(500).body(new ApiResponse<>(false, e.getMessage(), e));
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<ApiResponse<Exception>> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e) {
        return ResponseEntity.status(500).body(new ApiResponse<>(false, e.getMessage(), e));
    }

//    @ExceptionHandler(OAuth2AuthenticationException.class)
//    public ResponseEntity<ApiResponse<Exception>> handleOAuth2AuthenticationException(OAuth2AuthenticationException e) {
//        return ResponseEntity.status(500).body(new ApiResponse<>(false, e.getMessage(), e));
//    }
}

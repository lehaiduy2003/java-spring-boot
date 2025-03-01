package com.example.onlinecourses.exceptions.handlers;

import com.example.onlinecourses.dtos.responses.ApiResponse;
import com.example.onlinecourses.exceptions.NoPermissionException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class is used to handle exceptions related to authentication (401, 403)
 */
@RestControllerAdvice
public class AuthExHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<String>> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(401).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidTokenException(MalformedJwtException e) {
        return ResponseEntity.status(401).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ApiResponse<String>> handleUnsupportedJwtException(UnsupportedJwtException e) {
        return ResponseEntity.status(401).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<ApiResponse<String>> handleNoPermissionException(NoPermissionException e) {
        return ResponseEntity.status(403).body(new ApiResponse<>(false, e.getMessage(), null));
    }
}

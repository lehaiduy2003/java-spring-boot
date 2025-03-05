package com.example.onlinecourses.exceptions.handlers;

import com.example.onlinecourses.dtos.responses.ApiResponseDTO;
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
    public ResponseEntity<ApiResponseDTO<Throwable>> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(401).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleInvalidTokenException(MalformedJwtException e) {
        return ResponseEntity.status(401).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleUnsupportedJwtException(UnsupportedJwtException e) {
        return ResponseEntity.status(401).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleNoPermissionException(NoPermissionException e) {
        return ResponseEntity.status(403).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }
}

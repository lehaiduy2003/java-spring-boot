package com.example.onlinecourses.exceptions.handlers;

import com.example.onlinecourses.dtos.responses.ApiResponseDTO;
import com.example.onlinecourses.exceptions.PasswordNotMatchException;
import com.example.onlinecourses.exceptions.ResourceAlreadyExistException;
import com.example.onlinecourses.exceptions.ResourceNotFoundException;
import com.example.onlinecourses.exceptions.RoleNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class is used to handle exceptions related to bad requests (400, 404)
 */
@RestControllerAdvice
public class BadReqExHandler {
    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handlePasswordNotMatchException(PasswordNotMatchException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleResourceAlreadyExistException(ResourceAlreadyExistException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleRoleNotFoundException(RoleNotFoundException e) {
        return ResponseEntity.status(404).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseDTO<Throwable>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), e.getCause()));
    }
}

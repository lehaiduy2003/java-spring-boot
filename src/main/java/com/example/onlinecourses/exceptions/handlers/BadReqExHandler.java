package com.example.onlinecourses.exceptions.handlers;

import com.example.onlinecourses.dtos.responses.ApiResponse;
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
    public ResponseEntity<ApiResponse<String>> handlePasswordNotMatchException(PasswordNotMatchException e) {
        return ResponseEntity.status(400).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(400).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(400).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceAlreadyExistException(ResourceAlreadyExistException e) {
        return ResponseEntity.status(400).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleRoleNotFoundException(RoleNotFoundException e) {
        return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.status(400).body(new ApiResponse<>(false, e.getMessage(), null));
    }
}

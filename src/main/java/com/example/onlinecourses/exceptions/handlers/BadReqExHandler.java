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
    public ResponseEntity<ApiResponseDTO<String>> handlePasswordNotMatchException(PasswordNotMatchException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleResourceAlreadyExistException(ResourceAlreadyExistException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(new ApiResponseDTO<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleRoleNotFoundException(RoleNotFoundException e) {
        return ResponseEntity.status(404).body(new ApiResponseDTO<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, e.getMessage(), null));
    }
}

package com.example.onlinecourses.controllers;

import com.example.onlinecourses.dtos.ApiResponse;
import com.example.onlinecourses.dtos.reqMethod.post.UserCreationDTO;
import com.example.onlinecourses.dtos.models.UserDTO;
import com.example.onlinecourses.services.Interfaces.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        try {
            UserDTO createdUserDTO = userService.create(userCreationDTO);
            return ResponseEntity.status(201).body(new ApiResponse<>(true, "User created successfully", createdUserDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "An error occurred", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserById(@Valid @PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUserDTO = userService.updateById(id, userDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", updatedUserDTO));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "An error occurred", null));
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<Boolean>> deleteUserByEmail(@PathVariable String email) {
        try {
            Boolean isDeleted = userService.deleteByEmail(email);
            ApiResponse<Boolean> response;
            return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "An error occurred", null));
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        try {
            UserDTO userDTO = userService.findByEmail(email);
            return ResponseEntity.ok(new ApiResponse<>(true, "User data found", userDTO));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "An error occurred", null));
        }
    }

    @GetMapping
    public ResponseEntity<UserDTO[]> getAllUsers() {
        UserDTO[] userDTOS = userService.findMany(null);
        return ResponseEntity.ok(userDTOS);
    }
}
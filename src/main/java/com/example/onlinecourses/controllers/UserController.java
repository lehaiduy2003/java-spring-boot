package com.example.onlinecourses.controllers;

import com.example.onlinecourses.dtos.responses.ApiResponseDTO;
import com.example.onlinecourses.dtos.requests.post.UserCreationDTO;
import com.example.onlinecourses.dtos.models.UserDTO;
import com.example.onlinecourses.dtos.responses.data.UserDataDTO;
import com.example.onlinecourses.services.interfaces.IUserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserDTO>> createUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        UserDTO createdUserDTO = userService.create(userCreationDTO);
        return ResponseEntity.status(201).body(new ApiResponseDTO<>(true, "User created successfully", createdUserDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> updateUserById(@Valid @PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateById(id, userDTO);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "User updated successfully", updatedUserDTO));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponseDTO<Boolean>> deleteUserByEmail(@PathVariable String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "User deleted successfully", true));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getUserByEmail(@RequestParam(name = "email") String email) {
        UserDTO userDTO = userService.findByEmail(email);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "User data found", userDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<UserDataDTO[]>> getAllUsers(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        UserDataDTO[] userDTOS = userService.findMany(pageable);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Users data found", userDTOS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "User data found", userDTO));
    }
}
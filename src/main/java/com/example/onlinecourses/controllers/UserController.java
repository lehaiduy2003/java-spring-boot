package com.example.onlinecourses.controllers;

import com.example.onlinecourses.dtos.UserDTO;
import com.example.onlinecourses.services.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUserDTO = userService.create(userDTO);
        return ResponseEntity.ok(createdUserDTO);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserDTO> updateUserByEmail(@PathVariable String email, @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateByEmail(email, userDTO);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Boolean> deleteUserByEmail(@PathVariable String email) {
        Boolean isDeleted = userService.deleteByEmail(email);
        return ResponseEntity.ok(isDeleted);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.findByEmail(email);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    public ResponseEntity<UserDTO[]> getAllUsers() {
        UserDTO[] userDTOS = userService.findMany(null);
        return ResponseEntity.ok(userDTOS);
    }
}
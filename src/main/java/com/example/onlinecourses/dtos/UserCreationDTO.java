package com.example.onlinecourses.dtos;

import com.example.onlinecourses.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationDTO {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String fullName;
    private Set<Role> roles;
}

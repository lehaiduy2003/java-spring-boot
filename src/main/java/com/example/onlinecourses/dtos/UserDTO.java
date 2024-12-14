package com.example.onlinecourses.dtos;

import com.example.onlinecourses.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String fullName;
    private String address;
    private String avatar;
    private String bio;
    private Date dob;
    private boolean gender;
    private Set<Role> roles;
    // Getters and setters
}
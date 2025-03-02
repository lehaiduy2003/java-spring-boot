package com.example.onlinecourses.dtos.models;

import com.example.onlinecourses.models.Role;
import lombok.*;

import java.util.Date;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String fullname;
    private String address;
    private String avatar;
    private String bio;
    private Date dob;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private String username;
    private String password;
    private String phoneNumber;
    private boolean gender;
    private Set<Role> roles;
    // Getters and setters
}
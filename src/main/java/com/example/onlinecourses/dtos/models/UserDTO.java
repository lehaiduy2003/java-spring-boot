package com.example.onlinecourses.dtos.models;
import lombok.*;

import java.util.Date;
import java.util.Set;


@Getter
@Setter
@Builder
@ToString
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
    private Set<RoleDTO> roles;
    // Getters and setters
}
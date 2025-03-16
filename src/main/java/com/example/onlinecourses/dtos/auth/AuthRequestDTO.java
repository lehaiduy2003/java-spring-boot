package com.example.onlinecourses.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthRequestDTO {
    @Email(message = "Email should be valid")
    private String email;
    private String username;
    private String phoneNumber;
    @Size(min = 12, message = "Password must be longer than 12 characters")
    private String password;
}

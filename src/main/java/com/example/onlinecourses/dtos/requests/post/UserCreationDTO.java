package com.example.onlinecourses.dtos.requests.post;

import com.example.onlinecourses.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationDTO {
    @Email(message = "Email should be valid")
    private String email;
    private String username;
    @Size(min = 9, max = 11)
    private String phoneNumber;
    @Size(min = 6, message = "Password must be longer than 6 characters")
    private String password;
    private String fullname;
    private Set<Role> roles;
    @Past(message = "Date of birth must be in the past")
    private Date dob;
}

package com.example.onlinecourses.swagger.requests;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
public class SignUpRequest {
    @Email(message = "Email should be valid")
    private String email;
    private String fullname;
    @JsonIgnore
    private String username;
    private String password;
}

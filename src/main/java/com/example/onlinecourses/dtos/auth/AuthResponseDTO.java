package com.example.onlinecourses.dtos.auth;

import com.example.onlinecourses.dtos.responses.data.UserDataDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class AuthResponseDTO {
    private UserDataDTO user;
    private String accessToken;
    private String refreshToken;
}

package com.example.onlinecourses.dtos.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}

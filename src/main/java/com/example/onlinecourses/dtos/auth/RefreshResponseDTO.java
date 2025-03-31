package com.example.onlinecourses.dtos.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefreshResponseDTO {
    private String accessToken;
}

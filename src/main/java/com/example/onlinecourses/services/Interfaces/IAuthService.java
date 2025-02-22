package com.example.onlinecourses.services.Interfaces;

import com.example.onlinecourses.dtos.auth.AuthRequestDTO;
import com.example.onlinecourses.dtos.auth.AuthResponseDTO;
import com.example.onlinecourses.dtos.reqMethod.post.UserCreationDTO;

public interface IAuthService {
    AuthResponseDTO signIn(AuthRequestDTO authRequestDTO);
    AuthResponseDTO signUp(UserCreationDTO userCreationDTO);
    String refreshAccessToken(String refreshToken);
    void signOut(String refreshToken);
}

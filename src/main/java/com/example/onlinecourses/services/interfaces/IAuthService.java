package com.example.onlinecourses.services.interfaces;

import com.example.onlinecourses.dtos.auth.AuthRequestDTO;
import com.example.onlinecourses.dtos.auth.AuthResponseDTO;
import com.example.onlinecourses.dtos.requests.post.UserCreationDTO;

public interface IAuthService {
    AuthResponseDTO signIn(AuthRequestDTO authRequestDTO);
    AuthResponseDTO signUp(UserCreationDTO userCreationDTO);
    String refreshAccessToken(String refreshToken);
    void signOut(String refreshToken);
}

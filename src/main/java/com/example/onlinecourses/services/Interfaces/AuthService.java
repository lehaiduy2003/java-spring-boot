package com.example.onlinecourses.services.Interfaces;

import com.example.onlinecourses.dtos.AuthRequestDTO;
import com.example.onlinecourses.dtos.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO signIn(AuthRequestDTO authRequestDTO);
    AuthResponseDTO signUp(AuthRequestDTO authRequestDTO);
    AuthResponseDTO refreshToken(String refreshToken);
    void signOut(String refreshToken);
}

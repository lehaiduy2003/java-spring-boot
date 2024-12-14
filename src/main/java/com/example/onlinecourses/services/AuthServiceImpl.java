package com.example.onlinecourses.services;

import com.example.onlinecourses.dtos.AuthRequestDTO;
import com.example.onlinecourses.dtos.AuthResponseDTO;
import com.example.onlinecourses.services.Interfaces.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponseDTO signIn(AuthRequestDTO authRequestDTO) {
        return null;
    }

    @Override
    public AuthResponseDTO signUp(AuthRequestDTO authRequestDTO) {
        return null;
    }

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public void signOut(String refreshToken) {

    }
}

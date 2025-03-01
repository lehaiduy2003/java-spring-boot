package com.example.onlinecourses.backgroundJobs.interfaces;

public interface ITokenBlacklistService {
    void revokeToken(String token, long expirationTime);
    boolean isRevoked(String token);
}

package com.example.onlinecourses.services.interfaces;

public interface IRateLimitService {
    boolean isAllowed(String clientIp);
}

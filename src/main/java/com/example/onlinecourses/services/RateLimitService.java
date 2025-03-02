package com.example.onlinecourses.services;
import com.example.onlinecourses.services.interfaces.IRateLimitService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService implements IRateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    public RateLimitService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(String clientIp) {
        String key = "RATE_LIMIT:" + clientIp;
        Long count = redisTemplate.opsForValue().increment(key);

        if (count == null) {
            count = 0L;
        }

        if (count == 1) {
            // reset time (second)
            long windowTime = 60;
            redisTemplate.expire(key, windowTime, TimeUnit.SECONDS);
        }
        final int MAX_REQUESTS = 10;
        return count <= MAX_REQUESTS;
    }
}

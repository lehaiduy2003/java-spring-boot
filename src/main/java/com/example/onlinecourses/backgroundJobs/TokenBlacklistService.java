package com.example.onlinecourses.backgroundJobs;

import com.example.onlinecourses.backgroundJobs.interfaces.ITokenBlacklistService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

@Service
public class TokenBlacklistService implements ITokenBlacklistService {
    // object to synchronize the access to the blacklist
    private final Lock lock = new ReentrantLock();
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final RedisTemplate<String, String> redisTemplate;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Async
    public void revokeToken(String token, long expirationTime) {
        boolean isLocked = false;
        try {
            // try to acquire the lock within 3 second
            isLocked = lock.tryLock(3, TimeUnit.SECONDS);
            if (isLocked) {
                if (Boolean.FALSE.equals(redisTemplate.hasKey(token))) {
                    redisTemplate.opsForValue().set(token, "blacklisted", expirationTime, TimeUnit.MILLISECONDS);
                }
            } else {
                // if the lock is not acquired, log the message
                logger.info("Could not acquire lock when trying to revoke token, skipping...");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("Thread interrupted while trying to acquire lock");
        } finally {
            if (isLocked) {
                lock.unlock(); // unlock the lock after the operation is done
            }
        }
    }

    @Override
    public boolean isRevoked(String token) throws NullPointerException {
        return redisTemplate.hasKey(token);
    }
}

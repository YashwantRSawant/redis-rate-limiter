package org.ytech.ratelimiter.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId, String methodName, int limit, int duration, TimeUnit timeUnit) {
        String key = generateKey(userId, methodName, duration, timeUnit);

        Long currentCount = redisTemplate.opsForValue().increment(key);

        if (currentCount == null) {
            return false;
        }

        if (currentCount == 1) {
            // Set expiration only the first time
            redisTemplate.expire(key, duration, timeUnit);
        }

        return currentCount <= limit;
    }

    private String generateKey(String userId, String methodName, int duration, TimeUnit timeUnit) {
        long durationInMillis = timeUnit.toMillis(duration);
        long window = System.currentTimeMillis() / durationInMillis;
        return String.format("rate:%s:%s:%d", methodName, userId, window);
    }
}
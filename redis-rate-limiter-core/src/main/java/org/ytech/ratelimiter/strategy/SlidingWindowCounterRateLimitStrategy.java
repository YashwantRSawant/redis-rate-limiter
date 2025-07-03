package org.ytech.ratelimiter.strategy;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("SLIDING_WINDOW_COUNTER")
public class SlidingWindowCounterRateLimitStrategy implements RateLimitStrategy {

    private final StringRedisTemplate redisTemplate;

    public SlidingWindowCounterRateLimitStrategy(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(String userId, String methodName, int limit, int duration, TimeUnit unit) {
        long now = System.currentTimeMillis();
        long durationInMillis = unit.toMillis(duration);
        long currentWindow = now / durationInMillis;
        long previousWindow = currentWindow - 1;

        String keyCurrent = String.format("rate:counter:%s:%s:%d", methodName, userId, currentWindow);
        String keyPrev = String.format("rate:counter:%s:%s:%d", methodName, userId, previousWindow);

        Long countCurrent = redisTemplate.opsForValue().getOperations().opsForValue().increment(keyCurrent);
        redisTemplate.expire(keyCurrent, duration, unit);

        String prevCountStr = redisTemplate.opsForValue().get(keyPrev);
        long countPrev = prevCountStr != null ? Long.parseLong(prevCountStr) : 0;

        long timeIntoWindow = now % durationInMillis;
        double weight = (double) (durationInMillis - timeIntoWindow) / durationInMillis;

        double estimatedCount = countPrev * weight + (countCurrent != null ? countCurrent : 0);
        return estimatedCount <= limit;
    }
}
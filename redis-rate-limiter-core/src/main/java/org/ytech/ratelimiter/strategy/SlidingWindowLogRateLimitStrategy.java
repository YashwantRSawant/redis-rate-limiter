package org.ytech.ratelimiter.strategy;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("SLIDING_WINDOW_LOG")
public class SlidingWindowLogRateLimitStrategy implements RateLimitStrategy {

    private final StringRedisTemplate redisTemplate;

    public SlidingWindowLogRateLimitStrategy(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(String userId, String methodName, int limit, int duration, TimeUnit unit) {
        long now = System.currentTimeMillis();
        long windowStart = now - unit.toMillis(duration);
        String key = String.format("rate:log:%s:%s", methodName, userId);

        // Remove old entries
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStart);

        // Count remaining entries
        Long count = redisTemplate.opsForZSet().zCard(key);
        if (count != null && count >= limit) {
            return false;
        }

        // Add current timestamp
        redisTemplate.opsForZSet().add(key, String.valueOf(now), now);
        redisTemplate.expire(key, duration, unit);
        return true;
    }
}
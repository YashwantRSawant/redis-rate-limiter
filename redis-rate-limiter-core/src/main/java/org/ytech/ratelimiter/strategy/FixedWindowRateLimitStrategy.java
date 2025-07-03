package org.ytech.ratelimiter.strategy;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("FIXED_WINDOW")
public class FixedWindowRateLimitStrategy implements RateLimitStrategy {

    private final StringRedisTemplate redisTemplate;

    public FixedWindowRateLimitStrategy(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(String userId, String methodName, int limit, int duration, TimeUnit unit) {
        long window = System.currentTimeMillis() / unit.toMillis(duration);
        String key = String.format("rate:%s:%s:%d", methodName, userId, window);
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, duration, unit);
        }
        return count <= limit;
    }
}